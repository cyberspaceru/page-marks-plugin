package com.wiley.qa.pagemarks.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;

public class TitleCompletionContributor extends CompletionContributor {
    public TitleCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), new TitleCompletionProvider());
    }
}
