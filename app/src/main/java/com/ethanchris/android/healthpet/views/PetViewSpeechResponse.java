package com.ethanchris.android.healthpet.views;

public class PetViewSpeechResponse {
    private String mResponse;

    PetViewSpeechResponse(String response) {
        this.mResponse = response;
    }

    public PetViewSpeechResponseType parse() {
        if (mResponse.contains("how are you")) {
            return PetViewSpeechResponseType.HOW_ARE_YOU;
        } else if (mResponse.contains("hello")) {
            return PetViewSpeechResponseType.HELLO;
        } else if (mResponse.contains("did not accomplish") || mResponse.contains("didn't accomplish")) {
            return PetViewSpeechResponseType.GOAL_NOT_ACCOMPLISHED;
        } else if (mResponse.contains("did accomplish") || mResponse.contains("accomplished")) {
            return PetViewSpeechResponseType.GOAL_ACCOMPLISHED;
        } else {
            return PetViewSpeechResponseType.UNKNOWN;
        }
    }

    public static String getReply(PetViewSpeechResponseType response) {
        switch (response) {
            case HELLO:
                return "Hello!";
            case HOW_ARE_YOU:
                return "I am good! I hope you are well too.";
            case GOAL_ACCOMPLISHED:
                return "That's fantastic! Great job, have a goal point!";
            case GOAL_NOT_ACCOMPLISHED:
                return "That's okay, try again tomorrow!";
            default:
                return "Hmm, I don't understand that.";
        }
    }
}
