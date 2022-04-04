package com.ethanchris.android.healthpet.views;

public class PetViewSpeechResponse {
    private String mResponse;

    PetViewSpeechResponse(String response) {
        this.mResponse = response;
    }

    public PetViewSpeechResponseType parse() {
        if (mResponse.contains("how are you")) {
            return PetViewSpeechResponseType.HOW_ARE_YOU;
        } else if (mResponse.contains("your name")) {
            return PetViewSpeechResponseType.WHATS_YOUR_NAME;
        } else if (mResponse.contains("hello") || mResponse.contains("hi") || mResponse.contains("what's up")) {
            return PetViewSpeechResponseType.HELLO;
        } else if (mResponse.contains("did not accomplish") || mResponse.contains("didn't accomplish")) {
            return PetViewSpeechResponseType.GOAL_NOT_ACCOMPLISHED;
        } else if (mResponse.contains("did accomplish") || mResponse.contains("accomplished")) {
            return PetViewSpeechResponseType.GOAL_ACCOMPLISHED;
        } else {
            return PetViewSpeechResponseType.UNKNOWN;
        }
    }

    public static String getReply(PetViewSpeechResponseType response, String name) {
        switch (response) {
            case HELLO:
                return "Hello!";
            case HOW_ARE_YOU:
                return "I am good! I hope you are well too.";
            case GOAL_ACCOMPLISHED:
                return "That's fantastic! Great job.";
            case GOAL_NOT_ACCOMPLISHED:
                return "That's okay, try again tomorrow!";
            case WHATS_YOUR_NAME:
                return "My name is " + name + ".";
            default:
                return "Hmm, I don't understand that.";
        }
    }
}
