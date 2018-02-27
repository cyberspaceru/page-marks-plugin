package com.wiley.qa.pagemarks;

import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiModifierListImpl;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringMarkContext {
    private static final String TITLE_QUALIFIED_NAME = "com.wiley.qa.marks.Title";
    private static final String PAGE_PROVIDER_QUALIFIED_NAME = "com.wiley.qa.marks.TitleProvider";

    private PsiLiteralExpressionImpl literal;
    private PsiClass pageClass;

    private StringMarkContext(PsiLiteralExpressionImpl literal, PsiClass pageClass) {
        this.literal = literal;
        this.pageClass = pageClass;
    }

    public PsiLiteralExpressionImpl getLiteral() {
        return literal;
    }

    public PsiClass getPageClass() {
        return pageClass;
    }

    public Stream<PsiAnnotation> annotations() {
        return Arrays.stream(pageClass.getAllFields()).flatMap(x -> Arrays.stream(x.getAnnotations())).filter(x -> TITLE_QUALIFIED_NAME.equals(x.getQualifiedName()));
    }

    public List<String> findTitles() {
        return annotations()
                .map(x -> AnnotationUtil.getStringAttributeValue(x, "value"))
                .collect(Collectors.toList());
    }

    public PsiField findFieldByTitle(String title) {
        return annotations()
                .filter(x ->  {
                    String value = AnnotationUtil.getStringAttributeValue(x, "value");
                    return title != null && title.equals(value);
                })
                .map(PsiAnnotation::getOwner)
                .filter(x -> x instanceof PsiModifierListImpl)
                .map(x -> ((PsiModifierListImpl) x).getContext())
                .filter(x -> x instanceof PsiField)
                .map(x -> (PsiField) x)
                .findFirst()
                .orElse(null);
    }

    public static StringMarkContext toStringMarkContext(PsiLiteralExpressionImpl psiElement) {
        return Optional.ofNullable(psiElement)
                .map(x -> {
                    PsiMethod psiMethod = PsiTreeUtil.getParentOfType(x, PsiMethod.class);
                    if (psiMethod != null) {
                        PsiClass page = findPage(psiMethod);
                        if (page != null) {
                            return new StringMarkContext(x, page);
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .orElse(null);
    }

    /**
     * Find an annotation which is attached to a method.
     */
    private static PsiClass findPage(PsiMethod psiMethod) {
        return Arrays.stream(psiMethod.getAnnotations())
                .filter(x -> PAGE_PROVIDER_QUALIFIED_NAME.equals(x.getQualifiedName()))
                .map(x -> (PsiClassObjectAccessExpression) x.getParameterList().getAttributes()[0].getValue()).filter(Objects::nonNull)
                .map(PsiClassObjectAccessExpression::getOperand)
                .filter(Objects::nonNull)
                .map(PsiTypeElement::getType)
                .filter(Objects::nonNull)
                .map(PsiTypesUtil::getPsiClass)
                .findFirst()
                .orElse(null);
    }
}
