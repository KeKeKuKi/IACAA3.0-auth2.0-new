package com.gapache.commons.model;

/**
 * @author HuSen
 * @since 2020/8/10 10:29 上午
 */
public class AuthConstants {

    public static final String VEA = "vea-admin";
    public static final String ACCESS_CARD_HEADER = "_access_card";
    public static final String TOKEN_HEADER = "_token";
    public static final String X_FROM_HEADER = "_from";

    public static final String IS_ENABLED = "isEnabled";
    public static final String POSITION_ID = "positionId";
    public static final String SUPERIOR_ID = "superiorId";

    public static boolean filterCustomizeInfo(String key) {
        switch (key) {
            case IS_ENABLED:
            case POSITION_ID:
            case SUPERIOR_ID:
                return true;
            default:
                return false;
        }
    }

    public static class EventBusAddress {
        public static final String UPDATE_AUTHORIZE_INFO_ADDRESS = "update.authorize.info.address";
    }
}
