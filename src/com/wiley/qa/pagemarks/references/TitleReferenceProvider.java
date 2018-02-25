package com.wiley.qa.pagemarks.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.util.ProcessingContext;
import com.wiley.qa.pagemarks.StringMarkContext;
import org.jetbrains.annotations.NotNull;

public class TitleReferenceProvider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
        PsiLiteralExpressionImpl literalExpression = psiElement instanceof PsiLiteralExpressionImpl ? (PsiLiteralExpressionImpl) psiElement : null;
        StringMarkContext mark = literalExpression != null ? StringMarkContext.toStringMarkContext(literalExpression) : null;
        if (mark == null) {
            return PsiReference.EMPTY_ARRAY;
        }
        PsiField field = mark.findFieldByTitle(mark.getLiteral().getInnerText());
        if (field != null) {
            TitleReference reference = new TitleReference(field, new TextRange(0, literalExpression.getText().length()));
            return new PsiReference[]{reference};
        }
         return PsiReference.EMPTY_ARRAY;
    }
}
