package com.vicious.persist.io.parser;

import com.vicious.persist.except.ParserException;
import com.vicious.persist.io.parser.enums.CommentType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface IParser {
    default Map<String,Object> mappify(InputStream inputStream){
        return mappify(new TokenView(inputStream));
    }
    default List<Object> listify(InputStream inputStream){
        return listify(new TokenView(inputStream));
    }

    Map<String,Object> mappify(TokenView tokenView);
    List<Object> listify(TokenView tokenView);

    TokenView getTokenView();

    default char getLastToken(){
        return getTokenView().getLastToken();
    }
    default char getCurrentToken() {
        return getTokenView().getCurrentToken();
    }
    default char getNextToken() {
        return getTokenView().getNextToken();
    }

    CommentType getCommentState();
    void setCommentState(CommentType commentState);

    default CommentType getCommentType(){
        if(getCurrentToken() == '/'){
            if(getNextToken() == '*'){
                return CommentType.BLOCK;
            }
            else if(getNextToken() == '/'){
                return CommentType.SINGLE_LINE;
            }
        }
        return CommentType.NONE;
    }

    default boolean isCommentEnd(CommentType type){
        if(type == CommentType.NONE){
            return false;
        }
        else if(type == CommentType.SINGLE_LINE){
            return isNewline(getCurrentToken());
        }
        else {
            return getCurrentToken() == '/' && getLastToken() == '*';
        }
    }

    String newLines = "\n" + (char)13;
    default boolean isNewline(char character){
        return newLines.indexOf(character) > -1;
    }

    default boolean inComment(){
        return getCommentState() != CommentType.NONE;
    }

    default void updateCommentState(){
        if(getCommentState() == CommentType.NONE){
            setCommentState(getCommentType());
        }
        else if(getCommentState() != CommentType.NONE) {
            // Allow upgrading the comment level for syntax like ////*
            if(getCommentType() == CommentType.BLOCK){
                setCommentState(CommentType.BLOCK);
            }
            else if (isCommentEnd(getCommentState())) {
                if(getCommentState() == CommentType.BLOCK){
                    try {
                        getTokenView().read();
                    } catch (IOException e) {
                        throw new ParserException("Failed to skip block comment end",e);
                    }
                }
                setCommentState(CommentType.NONE);
            }
        }
    }

    default boolean notEscaped() {
        return getLastToken() != '\\';
    }

}
