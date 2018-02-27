package com.wiley.qa.pagemarks.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.impl.source.tree.java.PsiJavaTokenImpl;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.util.ProcessingContext;
import com.wiley.qa.pagemarks.StringMarkContext;
import org.jetbrains.annotations.NotNull;

public class TitleCompletionProvider extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet resultSet) {
        PsiJavaTokenImpl javaToken = parameters.getPosition() instanceof PsiJavaTokenImpl ? (PsiJavaTokenImpl) parameters.getPosition() : null;
        PsiLiteralExpressionImpl literalExpression = javaToken != null && javaToken.getContext() instanceof PsiLiteralExpressionImpl ? (PsiLiteralExpressionImpl) javaToken.getContext() : null;
        StringMarkContext mark = literalExpression != null ? StringMarkContext.toStringMarkContext(literalExpression) : null;
        if (mark != null) {
            mark.findTitles().forEach(x -> resultSet.addElement(LookupElementBuilder.create(x)));
            resultSet.stopHere();
        }
    }
}
