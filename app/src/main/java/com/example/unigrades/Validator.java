package com.example.unigrades;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Validator {
    private TextInputLayout textInputLayout;
    private ArrayList<Watcher> watchers;

    private Validator(TextInputLayout textInputLayout, ArrayList<Watcher> watchers) {
        this.textInputLayout = textInputLayout;
        this.watchers = watchers;
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = charSequence.toString();
                String result = check(input);
                textInputLayout.setError(result);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private String getError() {
        String input = textInputLayout.getEditText().getText().toString();
        return check(input);
    }

    public boolean validateIt() {
        String input = textInputLayout.getEditText().getText().toString();
        return check(input).equals("");
    }

    private String check(String input) {
        boolean result = true;
        for (Watcher watcher : watchers) {
            result = watcher.privateCheck(input);
            if (!result) {
                return watcher.error;
            }
        }
        return "";
    }


    public static class Builder {
        private TextInputLayout textInputLayout;
        private ArrayList<Watcher> watchers = new ArrayList<Watcher>();
        private boolean isAlreadyBuild = false;

        public static Builder make(@NonNull TextInputLayout textInputLayout){
            return new Builder(textInputLayout);
        }

        private Builder(@NonNull TextInputLayout textInputLayout){
            this.textInputLayout = textInputLayout;
        }

        public Builder addWatcher(Watcher watcher){
            if(!isAlreadyBuild){
                this.watchers.add(watcher);
            }
            return this;
        }

        public Validator build(){
            if (!isAlreadyBuild) {
                isAlreadyBuild = true;
                Validator v = addValidator(textInputLayout, watchers);
                return v;
            }
            return null;
        }

        private static Validator addValidator(TextInputLayout textInputLayout, ArrayList<Watcher> watchers) {
            Validator v = new Validator(textInputLayout, watchers);
            return v;
        }

    }
    public abstract static class Watcher{
        private String error;
        private Watcher(String error) {
            this.error = error;
        }
        public abstract boolean privateCheck(String input);
    }

    public static class WatcherEmail extends Watcher{

        public WatcherEmail(String error){
            super(error);
        }

        @Override
        public boolean privateCheck(String input) {
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            Pattern pat = Pattern.compile(ePattern);
            if(!pat.matcher(input).matches()){
                return false;
            }
            return true;
        }
    }

    public static class WatcherMinimumText extends Watcher{
        private int min;
        public  WatcherMinimumText(String error, int min){
            super(error);
            this.min = min;
        }

        @Override
        public boolean privateCheck(String input) {
            String ePattern = "^*.{" + min + ",}$";
            Pattern pat = Pattern.compile(ePattern);
            if(!pat.matcher(input).matches()){
                return false;
            }
            return true;
        }
    }

    public static class WatcherMaximumText extends Watcher{
        private int max;
        public  WatcherMaximumText(String error, int max){
            super(error);
            this.max = max;
        }

        @Override
        public boolean privateCheck(String input) {
            String ePattern = "^*.{0," + max + "}$";
            Pattern pat = Pattern.compile(ePattern);
            if(!pat.matcher(input).matches()){
                return false;
            }
            return true;
        }
    }

    public static class WatcherAtLeastOneNumber extends Watcher{
        public WatcherAtLeastOneNumber(String error){
            super(error);
        }

        @Override
        public boolean privateCheck(String input) {
            String ePattern = "^*(?=.*[0-9]).*$";
            Pattern pat = Pattern.compile(ePattern);
            if(!pat.matcher(input).matches()){
                return false;
            }
            return true;
        }


    }

    public static class WatcherAtLeastOneLowerCase extends Watcher{
        public WatcherAtLeastOneLowerCase(String error){
            super(error);
        }

        @Override
        public boolean privateCheck(String input) {
            String ePattern = "^*(?=.*[a-z]).*$";
            Pattern pat = Pattern.compile(ePattern);
            if(!pat.matcher(input).matches()){
                return false;
            }
            return true;
        }
    }

    public static class WatcherAtLeastOneUpperCase extends Watcher{
        public WatcherAtLeastOneUpperCase(String error){
            super(error);
        }

        @Override
        public boolean privateCheck(String input) {
            String ePattern = "^*(?=.*[A-Z]).*$";
            Pattern pat = Pattern.compile(ePattern);
            if(!pat.matcher(input).matches()){
                return false;
            }
            return true;
        }
    }

    public static class WatcherStartWithUpperCase extends Watcher{
        public WatcherStartWithUpperCase(String error){
            super(error);
        }

        @Override
        public boolean privateCheck(String input) {
            String ePattern = "^[A-Z].*$";
            Pattern pat = Pattern.compile(ePattern);
            if(!pat.matcher(input).matches()){
                return false;
            }
            return true;
        }
    }

    public static class WatcherIsANumber extends Watcher{
        public WatcherIsANumber(String error){
            super(error);
        }
        @Override
        public boolean privateCheck(String input) {
            String ePattern = "^\\d*\\.?\\d*$";
            Pattern pat = Pattern.compile(ePattern);
            if(!pat.matcher(input).matches()){
                return false;
            }
            return true;        }
    }
}

