package com.psyco.twitchwidget.twitchapi;

import com.psyco.twitchwidget.twitchapi.api.APIResponse;
import com.psyco.twitchwidget.twitchapi.api.PageableAPICall;

import java.util.List;

public class AccessorUtil {

    public static <E, T extends PageableAPICall<E>> void getAllPages(TwitchAPIAccessor accessor, String url, List<E> toAddTo, Class<T> clazz) throws Exception {
        PageableAPICall<E> response = getPage(accessor, url, clazz);
        toAddTo.addAll(response.getContents());
        int initialTotal = response.getTotal();
        int remaining = initialTotal - toAddTo.size();
        while (remaining > 0) {
            response = getPage(accessor, response.getNextURL(), clazz);

            if (response.getContents().size() == 0) {
                System.err.println("Notice: Paged response did not get expected number of items: initial total: "
                        + initialTotal + ", retrieved: " + toAddTo.size());
                return;
            }

            toAddTo.addAll(response.getContents());
            remaining -= response.getContents().size();
        }
    }

    private static <E, T extends PageableAPICall<E>> T getPage(TwitchAPIAccessor accessor, String nextURL, Class<T> clazz) throws Exception {
        APIResponse response = accessor.get(nextURL, clazz);
        if (response.isError()) {
            throw new RuntimeException("API resulted in an error");
        }

        @SuppressWarnings("unchecked")
        T cast = (T) response;
        return cast;
    }
}
