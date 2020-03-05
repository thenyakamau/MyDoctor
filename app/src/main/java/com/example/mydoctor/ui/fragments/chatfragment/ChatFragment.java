package com.example.mydoctor.ui.fragments.chatfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mydoctor.R;
import com.example.mydoctor.baseviews.BaseFragment;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatFragment extends BaseFragment {

    @BindView(R.id._chat_fragment_emoji_editText)
    EmojiEditText _chat_fragment_emoji_editText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_chat_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(view).build(_chat_fragment_emoji_editText);
        emojiPopup.toggle(); // Toggles visibility of the Popup.
        emojiPopup.dismiss(); // Dismisses the Popup.
        emojiPopup.isShowing(); // Returns true when Popup is showing.
    }
}
