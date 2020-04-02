package com.android.uraall.databinding;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Greeting extends BaseObservable {

    private String senderName;
    private String greetingText;

    public Greeting() {
    }

    public Greeting(String senderName, String greetingText) {
        this.senderName = senderName;
        this.greetingText = greetingText;
    }

    @Bindable
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
        notifyPropertyChanged(BR.senderName);
    }

    @Bindable
    public String getGreetingText() {
        return greetingText;
    }

    public void setGreetingText(String greetingText) {
        this.greetingText = greetingText;
        notifyPropertyChanged(BR.greetingText);
    }
}
