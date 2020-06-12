/*
 * Copyright (C) 2015-2016 Federico Tomassetti
 * Copyright (C) 2017-2020 The JavaParser Team.
 *
 * This file is part of JavaParser.
 *
 * JavaParser can be used either under the terms of
 * a) the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * b) the terms of the Apache License
 *
 * You should have received a copy of both licenses in LICENCE.LGPL and
 * LICENCE.APACHE. Please refer to those files for details.
 *
 * JavaParser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 */

package com.github.javaparser.symbolsolver.resolution;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests resolution of ObjectCreationExpr with complex scopes
 *
 * @author Takeshi D. Itoh
 */
class ObjectCreationExprResolutionTest extends AbstractResolutionTest {

    @BeforeAll
    static void configureSymbolSolver() throws IOException {
        StaticJavaParser.getConfiguration().setSymbolResolver(new JavaSymbolSolver(new ReflectionTypeSolver()));
    }

    @AfterAll
    static void unConfigureSymbolSolver() {
        // unconfigure symbol solver so as not to potentially disturb tests in other classes
        StaticJavaParser.getConfiguration().setSymbolResolver(null);
    }

    @Test
    void solveCast() throws IOException {
        CompilationUnit cu = parseSample("ObjectCreationExprResolution");
        ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Main");
        MethodDeclaration md = Navigator.demandMethod(clazz, "cast");
        MethodCallExpr mce = Navigator.findMethodCall(md, "method").get();
        String actual = mce.resolve().getQualifiedName();
        assertEquals("A.B.method", actual);
    }

    // @Test
    // void solveFieldAccess() throws IOException {
    //     CompilationUnit cu = parseSample("ObjectCreationExprResolution");
    //     ClassOrInterfaceDeclaration clazz = Navigator.demandClass(cu, "Main");
    //     MethodDeclaration md = Navigator.demandMethod(clazz, "fieldAccess");
    //     MethodCallExpr mce = Navigator.findMethodCall(md, "method").get();
    //     String actual = mce.resolve().getQualifiedName();
    //     assertEquals("A.B.C.method", actual);
    // }

}
