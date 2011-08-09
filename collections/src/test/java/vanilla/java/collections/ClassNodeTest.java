package vanilla.java.collections;

/*
 * Copyright 2011 Peter Lawrey
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import org.junit.Test;
import org.objectweb.asm.*;
import org.objectweb.asm.util.ASMifierClassVisitor;
import vanilla.java.collections.hand.MutableTypesAllocation;
import vanilla.java.collections.hand.MutableTypesArrayList;
import vanilla.java.collections.hand.MutableTypesElement;

import java.io.IOException;
import java.io.PrintWriter;

import static org.objectweb.asm.Opcodes.*;

public class ClassNodeTest {
    static final String collections = "vanilla/java/collections/";

    @Test
    public void test() throws IOException {
        for (Class clazz : new Class[]{MutableTypesArrayList.class, MutableTypesAllocation.class, MutableTypesElement.class}) {
            ClassReader cr = new ClassReader(clazz.getName());
            ASMifierClassVisitor cv = new ASMifierClassVisitor(new PrintWriter(System.out));
            cr.accept(cv, 0);
        }
    }

    public static byte[] dump() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, collections + "MutableTypesArrayList", "Lvanilla/java/collections/impl/AbstractHugeArrayList<Lvanilla/java/collections/MutableTypes;Lvanilla/java/collections/MutableTypesAllocation;Lvanilla/java/collections/MutableTypesElement;>;", collections + "impl/AbstractHugeArrayList", null);

        cw.visitSource("MutableTypesArrayList.java", null);

        {
            fv = cw.visitField(ACC_FINAL, "elementTypeFieldModel", "Lvanilla/java/collections/model/Enum8FieldModel;", "Lvanilla/java/collections/model/Enum8FieldModel<Ljava/lang/annotation/ElementType;>;", null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_FINAL, "stringEnumerated16FieldModel", "Lvanilla/java/collections/model/Enumerated16FieldModel;", "Lvanilla/java/collections/model/Enumerated16FieldModel<Ljava/lang/String;>;", null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(I)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(32, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, collections + "impl/AbstractHugeArrayList", "<init>", "(I)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(26, l1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitTypeInsn(NEW, collections + "model/Enum8FieldModel");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("elementType");
            mv.visitIntInsn(BIPUSH, 10);
            mv.visitLdcInsn(Type.getType("Ljava/lang/annotation/ElementType;"));
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/annotation/ElementType", "values", "()[Ljava/lang/annotation/ElementType;");
            mv.visitMethodInsn(INVOKESPECIAL, collections + "model/Enum8FieldModel", "<init>", "(Ljava/lang/String;ILjava/lang/Class;[Ljava/lang/Enum;)V");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesArrayList", "elementTypeFieldModel", "Lvanilla/java/collections/model/Enum8FieldModel;");
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(28, l2);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitTypeInsn(NEW, collections + "model/Enumerated16FieldModel");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("text");
            mv.visitIntInsn(BIPUSH, 11);
            mv.visitLdcInsn(Type.getType("Ljava/lang/String;"));
            mv.visitMethodInsn(INVOKESPECIAL, collections + "model/Enumerated16FieldModel", "<init>", "(Ljava/lang/String;ILjava/lang/Class;)V");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesArrayList", "stringEnumerated16FieldModel", "Lvanilla/java/collections/model/Enumerated16FieldModel;");
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(33, l3);
            mv.visitInsn(RETURN);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesArrayList;", null, l0, l4, 0);
            mv.visitLocalVariable("allocationSize", "I", null, l0, l4, 1);
            mv.visitMaxs(7, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PROTECTED, "createAllocation", "()Lvanilla/java/collections/MutableTypesAllocation;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(37, l0);
            mv.visitTypeInsn(NEW, collections + "MutableTypesAllocation");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesArrayList", "allocationSize", "I");
            mv.visitMethodInsn(INVOKESPECIAL, collections + "MutableTypesAllocation", "<init>", "(I)V");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesArrayList;", null, l0, l1, 0);
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PROTECTED, "createElement", "(J)Lvanilla/java/collections/MutableTypesElement;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(42, l0);
            mv.visitTypeInsn(NEW, collections + "MutableTypesElement");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(LLOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, collections + "MutableTypesElement", "<init>", "(Lvanilla/java/collections/impl/AbstractHugeArrayList;J)V");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesArrayList;", null, l0, l1, 0);
            mv.visitLocalVariable("n", "J", null, l0, l1, 1);
            mv.visitMaxs(5, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PROTECTED + ACC_BRIDGE + ACC_SYNTHETIC, "createElement", "(J)Lvanilla/java/collections/impl/AbstractHugeElement;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(25, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(LLOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, collections + "MutableTypesArrayList", "createElement", "(J)Lvanilla/java/collections/MutableTypesElement;");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesArrayList;", null, l0, l1, 0);
            mv.visitLocalVariable("x0", "J", null, l0, l1, 1);
            mv.visitMaxs(3, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PROTECTED + ACC_BRIDGE + ACC_SYNTHETIC, "createAllocation", "()Ljava/lang/Object;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(25, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, collections + "MutableTypesArrayList", "createAllocation", "()Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesArrayList;", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

    public static byte[] dump2() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, collections + "MutableTypesAllocation", null, "java/lang/Object", null);

        cw.visitSource("MutableTypesAllocation.java", null);

        {
            fv = cw.visitField(0, "m_boolean", "Ljava/nio/IntBuffer;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(0, "m_boolean2", "Ljava/nio/IntBuffer;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(0, "m_byte", "Ljava/nio/ByteBuffer;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(0, "m_byte2", "Ljava/nio/ByteBuffer;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(0, "m_char", "Ljava/nio/CharBuffer;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(0, "m_short", "Ljava/nio/ShortBuffer;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(0, "m_int", "Ljava/nio/IntBuffer;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(0, "m_float", "Ljava/nio/FloatBuffer;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(0, "m_long", "Ljava/nio/LongBuffer;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(0, "m_double", "Ljava/nio/DoubleBuffer;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(0, "m_elementType", "Ljava/nio/ByteBuffer;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(0, "m_string", "Ljava/nio/CharBuffer;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(I)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(37, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(38, l1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/BooleanFieldModel", "newArrayOfField", "(I)Ljava/nio/IntBuffer;");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesAllocation", "m_boolean", "Ljava/nio/IntBuffer;");
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(39, l2);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/Boolean2FieldModel", "newArrayOfField", "(I)Ljava/nio/IntBuffer;");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesAllocation", "m_boolean2", "Ljava/nio/IntBuffer;");
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(40, l3);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/ByteFieldModel", "newArrayOfField", "(I)Ljava/nio/ByteBuffer;");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesAllocation", "m_byte", "Ljava/nio/ByteBuffer;");
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLineNumber(41, l4);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/Byte2FieldModel", "newArrayOfField", "(I)Ljava/nio/ByteBuffer;");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesAllocation", "m_byte2", "Ljava/nio/ByteBuffer;");
            Label l5 = new Label();
            mv.visitLabel(l5);
            mv.visitLineNumber(42, l5);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/CharFieldModel", "newArrayOfField", "(I)Ljava/nio/CharBuffer;");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesAllocation", "m_char", "Ljava/nio/CharBuffer;");
            Label l6 = new Label();
            mv.visitLabel(l6);
            mv.visitLineNumber(43, l6);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/ShortFieldModel", "newArrayOfField", "(I)Ljava/nio/ShortBuffer;");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesAllocation", "m_short", "Ljava/nio/ShortBuffer;");
            Label l7 = new Label();
            mv.visitLabel(l7);
            mv.visitLineNumber(44, l7);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/IntFieldModel", "newArrayOfField", "(I)Ljava/nio/IntBuffer;");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesAllocation", "m_int", "Ljava/nio/IntBuffer;");
            Label l8 = new Label();
            mv.visitLabel(l8);
            mv.visitLineNumber(45, l8);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/FloatFieldModel", "newArrayOfField", "(I)Ljava/nio/FloatBuffer;");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesAllocation", "m_float", "Ljava/nio/FloatBuffer;");
            Label l9 = new Label();
            mv.visitLabel(l9);
            mv.visitLineNumber(46, l9);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/LongFieldModel", "newArrayOfField", "(I)Ljava/nio/LongBuffer;");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesAllocation", "m_long", "Ljava/nio/LongBuffer;");
            Label l10 = new Label();
            mv.visitLabel(l10);
            mv.visitLineNumber(47, l10);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/DoubleFieldModel", "newArrayOfField", "(I)Ljava/nio/DoubleBuffer;");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesAllocation", "m_double", "Ljava/nio/DoubleBuffer;");
            Label l11 = new Label();
            mv.visitLabel(l11);
            mv.visitLineNumber(48, l11);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/Enum8FieldModel", "newArrayOfField", "(I)Ljava/nio/ByteBuffer;");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesAllocation", "m_elementType", "Ljava/nio/ByteBuffer;");
            Label l12 = new Label();
            mv.visitLabel(l12);
            mv.visitLineNumber(49, l12);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/Enumerated16FieldModel", "newArrayOfField", "(I)Ljava/nio/CharBuffer;");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesAllocation", "m_string", "Ljava/nio/CharBuffer;");
            Label l13 = new Label();
            mv.visitLabel(l13);
            mv.visitLineNumber(50, l13);
            mv.visitInsn(RETURN);
            Label l14 = new Label();
            mv.visitLabel(l14);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesAllocation;", null, l0, l14, 0);
            mv.visitLocalVariable("allocationSize", "I", null, l0, l14, 1);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }


    public static byte[] dump3() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, collections + "MutableTypesElement", "Lvanilla/java/collections/impl/AbstractHugeElement<Lvanilla/java/collections/MutableTypesAllocation;>;Lvanilla/java/collections/MutableTypes;", collections + "impl/AbstractHugeElement", new String[]{collections + "MutableTypes"});

        cw.visitSource("MutableTypesElement.java", null);

        {
            fv = cw.visitField(0, "allocation", "Lvanilla/java/collections/MutableTypesAllocation;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lvanilla/java/collections/impl/AbstractHugeArrayList;J)V", "(Lvanilla/java/collections/impl/AbstractHugeArrayList<Lvanilla/java/collections/MutableTypes;Lvanilla/java/collections/MutableTypesAllocation;Lvanilla/java/collections/MutableTypesElement;>;J)V", null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(29, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(LLOAD, 2);
            mv.visitMethodInsn(INVOKESPECIAL, collections + "impl/AbstractHugeElement", "<init>", "(Lvanilla/java/collections/impl/AbstractHugeArrayList;J)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(26, l1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(ACONST_NULL);
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(30, l2);
            mv.visitInsn(RETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l3, 0);
            mv.visitLocalVariable("list", "Lvanilla/java/collections/impl/AbstractHugeArrayList;", "Lvanilla/java/collections/impl/AbstractHugeArrayList<Lvanilla/java/collections/MutableTypes;Lvanilla/java/collections/MutableTypesAllocation;Lvanilla/java/collections/MutableTypesElement;>;", l0, l3, 1);
            mv.visitLocalVariable("n", "J", null, l0, l3, 2);
            mv.visitMaxs(4, 4);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setBoolean", "(Z)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(34, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_boolean", "Ljava/nio/IntBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/BooleanFieldModel", "set", "(Ljava/nio/IntBuffer;IZ)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(35, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("b", "Z", null, l0, l2, 1);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getBoolean", "()Z", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(39, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_boolean", "Ljava/nio/IntBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/BooleanFieldModel", "get", "(Ljava/nio/IntBuffer;I)Z");
            mv.visitInsn(IRETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l1, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setBoolean2", "(Ljava/lang/Boolean;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(44, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_boolean2", "Ljava/nio/IntBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/Boolean2FieldModel", "set", "(Ljava/nio/IntBuffer;ILjava/lang/Boolean;)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(45, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("b", "Ljava/lang/Boolean;", null, l0, l2, 1);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getBoolean2", "()Ljava/lang/Boolean;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(49, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_boolean2", "Ljava/nio/IntBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/Boolean2FieldModel", "get", "(Ljava/nio/IntBuffer;I)Ljava/lang/Boolean;");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l1, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setByte", "(B)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(54, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_byte", "Ljava/nio/ByteBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/ByteFieldModel", "set", "(Ljava/nio/ByteBuffer;ILjava/lang/Byte;)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(55, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("b", "B", null, l0, l2, 1);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getByte", "()B", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(59, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_byte", "Ljava/nio/ByteBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/ByteFieldModel", "get", "(Ljava/nio/ByteBuffer;I)Ljava/lang/Byte;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B");
            mv.visitInsn(IRETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l1, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setByte2", "(Ljava/lang/Byte;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(64, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_byte2", "Ljava/nio/ByteBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/Byte2FieldModel", "set", "(Ljava/nio/ByteBuffer;ILjava/lang/Byte;)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(65, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("b", "Ljava/lang/Byte;", null, l0, l2, 1);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getByte2", "()Ljava/lang/Byte;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(69, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_byte2", "Ljava/nio/ByteBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/Byte2FieldModel", "get", "(Ljava/nio/ByteBuffer;I)Ljava/lang/Byte;");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l1, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setChar", "(C)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(74, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_char", "Ljava/nio/CharBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/CharFieldModel", "set", "(Ljava/nio/CharBuffer;IC)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(75, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("ch", "C", null, l0, l2, 1);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getChar", "()C", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(79, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_char", "Ljava/nio/CharBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/CharFieldModel", "get", "(Ljava/nio/CharBuffer;I)C");
            mv.visitInsn(IRETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l1, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setShort", "(S)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(84, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_short", "Ljava/nio/ShortBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/ShortFieldModel", "set", "(Ljava/nio/ShortBuffer;IS)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(85, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("s", "S", null, l0, l2, 1);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getShort", "()S", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(89, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_short", "Ljava/nio/ShortBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/ShortFieldModel", "get", "(Ljava/nio/ShortBuffer;I)S");
            mv.visitInsn(IRETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l1, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setInt", "(I)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(94, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_int", "Ljava/nio/IntBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/IntFieldModel", "set", "(Ljava/nio/IntBuffer;II)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(95, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("i", "I", null, l0, l2, 1);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getInt", "()I", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(99, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_int", "Ljava/nio/IntBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/IntFieldModel", "get", "(Ljava/nio/IntBuffer;I)I");
            mv.visitInsn(IRETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l1, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setFloat", "(F)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(104, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_float", "Ljava/nio/FloatBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitVarInsn(FLOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/FloatFieldModel", "set", "(Ljava/nio/FloatBuffer;ILjava/lang/Float;)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(105, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("f", "F", null, l0, l2, 1);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getFloat", "()F", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(109, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_float", "Ljava/nio/FloatBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/FloatFieldModel", "get", "(Ljava/nio/FloatBuffer;I)Ljava/lang/Float;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F");
            mv.visitInsn(FRETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l1, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setLong", "(J)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(114, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_long", "Ljava/nio/LongBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitVarInsn(LLOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/LongFieldModel", "set", "(Ljava/nio/LongBuffer;IJ)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(115, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("l", "J", null, l0, l2, 1);
            mv.visitMaxs(4, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getLong", "()J", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(119, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_long", "Ljava/nio/LongBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/LongFieldModel", "get", "(Ljava/nio/LongBuffer;I)J");
            mv.visitInsn(LRETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l1, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setDouble", "(D)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(124, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_double", "Ljava/nio/DoubleBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitVarInsn(DLOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/DoubleFieldModel", "set", "(Ljava/nio/DoubleBuffer;ILjava/lang/Double;)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(125, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("d", "D", null, l0, l2, 1);
            mv.visitMaxs(4, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getDouble", "()D", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(129, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_double", "Ljava/nio/DoubleBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitMethodInsn(INVOKESTATIC, collections + "model/DoubleFieldModel", "get", "(Ljava/nio/DoubleBuffer;I)Ljava/lang/Double;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D");
            mv.visitInsn(DRETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l1, 0);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setElementType", "(Ljava/lang/annotation/ElementType;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(134, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "list", "Lvanilla/java/collections/impl/AbstractHugeArrayList;");
            mv.visitTypeInsn(CHECKCAST, collections + "MutableTypesArrayList");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesArrayList", "elementTypeFieldModel", "Lvanilla/java/collections/model/Enum8FieldModel;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_elementType", "Ljava/nio/ByteBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, collections + "model/Enum8FieldModel", "set", "(Ljava/nio/ByteBuffer;ILjava/lang/Enum;)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(135, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("elementType", "Ljava/lang/annotation/ElementType;", null, l0, l2, 1);
            mv.visitMaxs(4, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getElementType", "()Ljava/lang/annotation/ElementType;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(139, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "list", "Lvanilla/java/collections/impl/AbstractHugeArrayList;");
            mv.visitTypeInsn(CHECKCAST, collections + "MutableTypesArrayList");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesArrayList", "elementTypeFieldModel", "Lvanilla/java/collections/model/Enum8FieldModel;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_elementType", "Ljava/nio/ByteBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitMethodInsn(INVOKEVIRTUAL, collections + "model/Enum8FieldModel", "get", "(Ljava/nio/ByteBuffer;I)Ljava/lang/Enum;");
            mv.visitTypeInsn(CHECKCAST, "java/lang/annotation/ElementType");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l1, 0);
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "setString", "(Ljava/lang/String;)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(144, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "list", "Lvanilla/java/collections/impl/AbstractHugeArrayList;");
            mv.visitTypeInsn(CHECKCAST, collections + "MutableTypesArrayList");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesArrayList", "stringEnumerated16FieldModel", "Lvanilla/java/collections/model/Enumerated16FieldModel;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_string", "Ljava/nio/CharBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, collections + "model/Enumerated16FieldModel", "set", "(Ljava/nio/CharBuffer;ILjava/lang/Object;)V");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(145, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("text", "Ljava/lang/String;", null, l0, l2, 1);
            mv.visitMaxs(4, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "getString", "()Ljava/lang/String;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(149, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "list", "Lvanilla/java/collections/impl/AbstractHugeArrayList;");
            mv.visitTypeInsn(CHECKCAST, collections + "MutableTypesArrayList");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesArrayList", "stringEnumerated16FieldModel", "Lvanilla/java/collections/model/Enumerated16FieldModel;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesAllocation", "m_string", "Ljava/nio/CharBuffer;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "offset", "I");
            mv.visitMethodInsn(INVOKEVIRTUAL, collections + "model/Enumerated16FieldModel", "get", "(Ljava/nio/CharBuffer;I)Ljava/lang/Object;");
            mv.visitTypeInsn(CHECKCAST, "java/lang/String");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l1, 0);
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PROTECTED, "updateAllocation0", "(I)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(154, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "list", "Lvanilla/java/collections/impl/AbstractHugeArrayList;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, collections + "MutableTypesElement", "index", "J");
            mv.visitMethodInsn(INVOKEVIRTUAL, collections + "impl/AbstractHugeArrayList", "getAllocation", "(J)Ljava/lang/Object;");
            mv.visitTypeInsn(CHECKCAST, collections + "MutableTypesAllocation");
            mv.visitFieldInsn(PUTFIELD, collections + "MutableTypesElement", "allocation", "Lvanilla/java/collections/MutableTypesAllocation;");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(155, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "Lvanilla/java/collections/MutableTypesElement;", null, l0, l2, 0);
            mv.visitLocalVariable("allocationSize", "I", null, l0, l2, 1);
            mv.visitMaxs(4, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}
