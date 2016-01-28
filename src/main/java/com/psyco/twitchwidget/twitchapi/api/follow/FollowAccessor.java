package com.psyco.twitchwidget.twitchapi.api.follow;

import com.psyco.twitchwidget.twitchapi.AccessorUtil;
import com.psyco.twitchwidget.twitchapi.TwitchAPIAccessor;
import com.psyco.twitchwidget.twitchapi.api.stream.StreamQueryResponse;
import com.psyco.twitchwidget.twitchapi.api.stream.StreamResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FollowAccessor {

    private final TwitchAPIAccessor accessor;
    private final String USERS_FOLLOWED_CHANNELS_URL = TwitchAPIAccessor.ROOT_URL + "/users/%s/follows/channels?limit=100";
    private final String CHANNELS_FOLLOWED_USERS_URL = TwitchAPIAccessor.ROOT_URL + "/channels/%s/follows?limit=100";
    private final String FOLLOW_URL = TwitchAPIAccessor.ROOT_URL + "/users/%s/follows/channels/%s";
    private final String USER_STREAMS_FOLLOWED_URL = TwitchAPIAccessor.ROOT_URL + "/streams/followed?limit=100";

    public FollowAccessor(TwitchAPIAccessor accessor) {
        this.accessor = accessor;
    }

    public void getUsersFollowedChannels(String username, Consumer<List<ChannelFollowResponse>> onSuccess, Consumer<Exception> onException) {
        List<ChannelFollowResponse> list = new ArrayList<>();
        accessor.submitTask(() -> AccessorUtil.getAllPages(accessor, String.format(USERS_FOLLOWED_CHANNELS_URL, username), list, UsersFollowedChannelsResponse.class))
                .withOnSucceeded(v -> onSuccess.accept(list))
                .withOnException(onException::accept)
                .start();
    }

    public void getChannelsFollowedUsers(String channel, Consumer<List<UserFollowResponse>> onSuccess, Consumer<Exception> onException) {
        List<UserFollowResponse> list = new ArrayList<>();
        accessor.submitTask(() -> AccessorUtil.getAllPages(accessor, String.format(CHANNELS_FOLLOWED_USERS_URL, channel), list, ChannelsFollowedUsersResponse.class))
                .withOnSucceeded(v -> onSuccess.accept(list))
                .withOnException(onException::accept)
                .start();
    }

    public void getFollowStatus(String username, String channel, Consumer<ChannelFollowResponse> onSuccess, Consumer<Exception> onException) {
        accessor.submitTask(() -> accessor.get(String.format(FOLLOW_URL, username, channel), ChannelFollowResponse.class))
                .withOnSucceeded(resp -> onSuccess.accept((ChannelFollowResponse) resp))
                .withOnException(onException::accept)
                .start();
    }

    public void getUserStreamsFollowed(Consumer<List<StreamResponse>> onSuccess, Consumer<Exception> onException) {
        List<StreamResponse> list = new ArrayList<>();
        accessor.submitTask(() -> AccessorUtil.getAllPages(accessor, USER_STREAMS_FOLLOWED_URL, list, StreamQueryResponse.class))
                .withOnSucceeded(v -> onSuccess.accept(list))
                .withOnException(onException::accept)
                .start();
    }
}
