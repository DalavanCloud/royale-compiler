package org.apache.flex.compiler.internal.codegen.js.sourcemaps;

import org.apache.flex.compiler.driver.IBackend;
import org.apache.flex.compiler.internal.driver.js.flexjs.FlexJSBackend;
import org.apache.flex.compiler.internal.test.SourceMapTestBase;
import org.apache.flex.compiler.tree.as.IForLoopNode;
import org.apache.flex.compiler.tree.as.IVariableNode;

import org.junit.Test;

public class TestSourceMapStatements extends SourceMapTestBase
{
    //----------------------------------
    // var declaration
    //----------------------------------

    @Test
    public void testVarDeclaration()
    {
        IVariableNode node = (IVariableNode) getNode("var a;",
                IVariableNode.class);
        asBlockWalker.visitVariable(node);
        //var /** @type {*} */ a
        assertMapping(node, 0, 0, 0, 0, 0, 4);   // var
        assertMapping(node, 0, 4, 0, 21, 0, 22); // a
        assertMapping(node, 0, 5, 0, 4, 0, 21);  // (type)
    }

    @Test
    public void testVarDeclaration_withAssignedValue()
    {
        IVariableNode node = (IVariableNode) getNode("var a = 42;",
                IVariableNode.class);
        asBlockWalker.visitVariable(node);
        //var /** @type {*} */ a = 42
        assertMapping(node, 0, 0, 0, 0, 0, 4);   // var
        assertMapping(node, 0, 4, 0, 21, 0, 22); // a
        assertMapping(node, 0, 5, 0, 4, 0, 21);  // (type)
        assertMapping(node, 0, 5, 0, 22, 0, 25); // =
        assertMapping(node, 0, 8, 0, 25, 0, 27); // 42
    }

    @Test
    public void testVarDeclaration_withType()
    {
        IVariableNode node = (IVariableNode) getNode("var a:int;",
                IVariableNode.class);
        asBlockWalker.visitVariable(node);
        //var /** @type {number} */ a
        assertMapping(node, 0, 0, 0, 0, 0, 4);   // var
        assertMapping(node, 0, 4, 0, 26, 0, 27); // a
        assertMapping(node, 0, 5, 0, 4, 0, 26);  // :int
    }

    @Test
    public void testVarDeclaration_withTypeAssignedValue()
    {
        IVariableNode node = (IVariableNode) getNode("var a:int = 42;",
                IVariableNode.class);
        asBlockWalker.visitVariable(node);
        //var /** @type {number} */ a = 42
        assertMapping(node, 0, 0, 0, 0, 0, 4);    // var
        assertMapping(node, 0, 4, 0, 26, 0, 27);  // a
        assertMapping(node, 0, 5, 0, 4, 0, 26);   // :int
        assertMapping(node, 0, 9, 0, 27, 0, 30);  // =
        assertMapping(node, 0, 12, 0, 30, 0, 32); // 42
    }

    @Test
    public void testVarDeclaration_withTypeAssignedValueComplex()
    {
        IVariableNode node = (IVariableNode) getNode(
                "class A { public function b():void { var a:Foo = new Foo(42, 'goo');}} class Foo {}", IVariableNode.class, WRAP_LEVEL_PACKAGE);
        asBlockWalker.visitVariable(node);
        //var /** @type {Foo} */ a = new Foo(42, 'goo')
        assertMapping(node, 0, 0, 0, 0, 0, 4);    // var
        assertMapping(node, 0, 4, 0, 23, 0, 24);  // a
        assertMapping(node, 0, 5, 0, 4, 0, 23);   // :Foo
        assertMapping(node, 0, 9, 0, 24, 0, 27);  // =
        assertMapping(node, 0, 12, 0, 27, 0, 31);  // new
        assertMapping(node, 0, 16, 0, 31, 0, 34);  // Foo
        assertMapping(node, 0, 19, 0, 34, 0, 35);  // (
        assertMapping(node, 0, 20, 0, 35, 0, 37);  // 42
        assertMapping(node, 0, 22, 0, 37, 0, 39);  // ,
        assertMapping(node, 0, 24, 0, 39, 0, 44);  // 'goo'
        assertMapping(node, 0, 29, 0, 44, 0, 45);  // )
    }

    @Test
    public void testVarDeclaration_withList()
    {
        IVariableNode node = (IVariableNode) getNode(
                "var a:int = 4, b:int = 11, c:int = 42;", IVariableNode.class);
        asBlockWalker.visitVariable(node);
        //var /** @type {number} */ a = 4, /** @type {number} */ b = 11, /** @type {number} */ c = 42
        assertMapping(node, 0, 0, 0, 0, 0, 4);    // var
        assertMapping(node, 0, 4, 0, 26, 0, 27);  // a
        assertMapping(node, 0, 5, 0, 4, 0, 26);   // :int
        assertMapping(node, 0, 9, 0, 27, 0, 30);  // =
        assertMapping(node, 0, 12, 0, 30, 0, 31); // 4
        assertMapping(node, 0, 13, 0, 31, 0, 33); // ,
        assertMapping(node, 0, 15, 0, 55, 0, 56); // b
        assertMapping(node, 0, 16, 0, 33, 0, 55); // :int
        assertMapping(node, 0, 20, 0, 56, 0, 59); // =
        assertMapping(node, 0, 23, 0, 59, 0, 61); // 11
        assertMapping(node, 0, 25, 0, 61, 0, 63); // ,
        assertMapping(node, 0, 27, 0, 85, 0, 86); // c
        assertMapping(node, 0, 28, 0, 63, 0, 85); // :int
        assertMapping(node, 0, 32, 0, 86, 0, 89); // =
        assertMapping(node, 0, 35, 0, 89, 0, 91); // 42
    }

    //----------------------------------
    // for () { }
    //----------------------------------

    @Test
    public void testVisitFor_1a()
    {
        IForLoopNode node = (IForLoopNode) getNode(
                "for (var i:int = 0; i < len; i++) { break; }",
                IForLoopNode.class);
        asBlockWalker.visitForLoop(node);
        //for (var /** @type {number} */ i = 0; i < len; i++) {\n  break;\n}
        assertMapping(node, 0, 0, 0, 0, 0, 5);    // for (
        assertMapping(node, 0, 18, 0, 36, 0, 38); // ;
        assertMapping(node, 0, 27, 0, 45, 0, 47); // ;
        assertMapping(node, 0, 32, 0, 50, 0, 52); // )
    }

    @Test
    public void testVisitFor_1b()
    {
        IForLoopNode node = (IForLoopNode) getNode(
                "for (var i:int = 0; i < len; i++) break;", IForLoopNode.class);
        asBlockWalker.visitForLoop(node);
        //for (var /** @type {number} */ i = 0; i < len; i++)\n  break;
        assertMapping(node, 0, 0, 0, 0, 0, 5);    // for (
        assertMapping(node, 0, 18, 0, 36, 0, 38); // ;
        assertMapping(node, 0, 27, 0, 45, 0, 47); // ;
        assertMapping(node, 0, 32, 0, 50, 0, 51); // )
    }

    @Test
    public void testVisitFor_2()
    {
        IForLoopNode node = (IForLoopNode) getNode("for (;;) { break; }",
                IForLoopNode.class);
        asBlockWalker.visitForLoop(node);
        //for (;;) {\n  break;\n}
        assertMapping(node, 0, 0, 0, 0, 0, 5); // for (
        assertMapping(node, 0, 5, 0, 5, 0, 6); // ;
        assertMapping(node, 0, 6, 0, 6, 0, 7); // ;
        assertMapping(node, 0, 7, 0, 7, 0, 9); // )
    }

    @Test
    public void testVisitForIn_1()
    {
        IForLoopNode node = (IForLoopNode) getNode(
                "for (var i:int in obj) { break; }", IForLoopNode.class);
        asBlockWalker.visitForLoop(node);
        //for (var /** @type {number} */ i in obj) {\n  break;\n}
        assertMapping(node, 0, 0, 0, 0, 0, 5);    // for (
        assertMapping(node, 0, 14, 0, 32, 0, 36); // in
        assertMapping(node, 0, 21, 0, 39, 0, 41); // )
    }

    @Test
    public void testVisitForIn_1a()
    {
        IForLoopNode node = (IForLoopNode) getNode(
                "for (var i:int in obj)  break; ", IForLoopNode.class);
        asBlockWalker.visitForLoop(node);
        //for (var /** @type {number} */ i in obj)\n  break;
        assertMapping(node, 0, 0, 0, 0, 0, 5);    // for (
        assertMapping(node, 0, 14, 0, 32, 0, 36); // in
        assertMapping(node, 0, 21, 0, 39, 0, 40); // )
    }

    protected IBackend createBackend()
    {
        return new FlexJSBackend();
    }
}
