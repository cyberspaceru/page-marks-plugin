package com.wiley.qa.pagemarks.references;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TitleReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference{
    PsiField field;

    public TitleReference(@NotNull PsiField element, TextRange textRange) {
        super(element, textRange);
        this.field = element;
    }

    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @Override
    public Object[] getVariants() {
        List<LookupElement> variants = new ArrayList<>();
        variants.add(LookupElementBuilder.create(field).withTypeText(field.getName()));
        return variants.toArray();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean b) {
        return new ResolveResult[]{new PsiElementResolveResult(field)};
    }
}
