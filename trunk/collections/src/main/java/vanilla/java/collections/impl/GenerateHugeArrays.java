package vanilla.java.collections.impl;

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

import org.objectweb.asm.*;
import vanilla.java.collections.model.*;

import static org.objectweb.asm.Opcodes.*;

public enum GenerateHugeArrays {
    ;
    private static final String collections = "vanilla/java/collections/";

    public static byte[] dumpArrayList(TypeModel tm) {
        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;

        Class interfaceClass = tm.type();
        String name = interfaceClass.getName().replace('.', '/');

        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, name + "ArrayList", "L" + collections + "impl/AbstractHugeArrayList<L" + name + ";L" + name + "Allocation;L" + name + "Element;>;", collections + "impl/AbstractHugeArrayList", null);

        cw.visitSource(tm.type().getSimpleName() + "ArrayList.java", null);

        for (FieldModel fm : tm.fields()) {
            if (fm instanceof Enum8FieldModel) {
                fv = cw.visitField(ACC_FINAL, fm.fieldName() + "FieldModel", "L" + collections + "model/Enum8FieldModel;", "L" + collections + "model/Enum8FieldModel<Ljava/lang/annotation/ElementType;>;", null);
                fv.visitEnd();
            } else if (fm instanceof Enumerated16FieldModel) {
                fv = cw.visitField(ACC_FINAL, fm.fieldName() + "FieldModel", "L" + collections + "model/Enumerated16FieldModel;", "L" + collections + "model/Enumerated16FieldModel<Ljava/lang/String;>;", null);
                fv.visitEnd();
            }
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(I)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(23, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, collections + "impl/AbstractHugeArrayList", "<init>", "(I)V");

            for (FieldModel fm : tm.fields()) {
                if (fm instanceof Enum8FieldModel) {
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitTypeInsn(NEW, collections + "model/Enum8FieldModel");
                    mv.visitInsn(DUP);
                    mv.visitLdcInsn(fm.fieldName());
                    mv.visitIntInsn(BIPUSH, fm.fieldNumber());
                    mv.visitLdcInsn(Type.getType(fm.bcLFieldType()));
                    mv.visitMethodInsn(INVOKESTATIC, fm.bcFieldType(), "values", "()[" + fm.bcLFieldType());
                    mv.visitMethodInsn(INVOKESPECIAL, collections + "model/Enum8FieldModel", "<init>", "(Ljava/lang/String;ILjava/lang/Class;[Ljava/lang/Enum;)V");
                    mv.visitFieldInsn(PUTFIELD, name + "ArrayList", fm.fieldName() + "FieldModel", 'L' + collections + "model/Enum8FieldModel;");

                } else if (fm instanceof Enumerated16FieldModel) {
                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitTypeInsn(NEW, collections + "model/Enumerated16FieldModel");
                    mv.visitInsn(DUP);
                    mv.visitLdcInsn(fm.fieldName());
                    mv.visitIntInsn(BIPUSH, fm.fieldNumber());
                    mv.visitLdcInsn(Type.getType(fm.bcLFieldType()));
                    mv.visitMethodInsn(INVOKESPECIAL, collections + "model/Enumerated16FieldModel", "<init>", "(Ljava/lang/String;ILjava/lang/Class;)V");
                    mv.visitFieldInsn(PUTFIELD, name + "ArrayList", fm.fieldName() + "FieldModel", 'L' + collections + "model/Enumerated16FieldModel;");
                }
            }

            mv.visitInsn(RETURN);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLocalVariable("this", "L" + name + "ArrayList;", null, l0, l4, 0);
            mv.visitLocalVariable("allocationSize", "I", null, l0, l4, 1);
            mv.visitMaxs(7, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PROTECTED, "createAllocation", "()L" + name + "Allocation;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(37, l0);
            mv.visitTypeInsn(NEW, name + "Allocation");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, name + "ArrayList", "allocationSize", "I");
            mv.visitMethodInsn(INVOKESPECIAL, name + "Allocation", "<init>", "(I)V");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "L" + name + "ArrayList;", null, l0, l1, 0);
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PROTECTED, "createElement", "(J)L" + name + "Element;", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(42, l0);
            mv.visitTypeInsn(NEW, name + "Element");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(LLOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, name + "Element", "<init>", "(Lvanilla/java/collections/impl/AbstractHugeArrayList;J)V");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "L" + name + "ArrayList;", null, l0, l1, 0);
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
            mv.visitMethodInsn(INVOKEVIRTUAL, name + "ArrayList", "createElement", "(J)L" + name + "Element;");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "L" + name + "ArrayList;", null, l0, l1, 0);
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
            mv.visitMethodInsn(INVOKEVIRTUAL, name + "ArrayList", "createAllocation", "()L" + name + "Allocation;");
            mv.visitInsn(ARETURN);
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLocalVariable("this", "L" + name + "ArrayList;", null, l0, l1, 0);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        cw.visitEnd();
        return cw.toByteArray();
    }

    public static byte[] dumpAllocation(TypeModel tm) {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;

        Class interfaceClass = tm.type();
        String name = interfaceClass.getName().replace('.', '/');

        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, name + "Allocation", null, "java/lang/Object", null);

        cw.visitSource(tm.type().getSimpleName() + "Allocation.java", null);

        for (FieldModel fm : tm.fields()) {
            fv = cw.visitField(0, "m_" + fm.fieldName(), fm.bcLStoreType(), null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(I)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            Label l0 = new Label();
            mv.visitLabel(l0);

            for (FieldModel fm : tm.fields()) {
                mv.visitVarInsn(ALOAD, 0);

                if (fm instanceof ObjectFieldModel) {
                    mv.visitLdcInsn(Type.getType(fm.bcLFieldType()));
                    mv.visitVarInsn(ILOAD, 1);
                    mv.visitMethodInsn(INVOKESTATIC, fm.getClass().getName().replace('.', '/'), "newArrayOfField", "(Ljava/lang/Class;I)[Ljava/lang/Object;");
                    mv.visitTypeInsn(CHECKCAST, fm.bcLStoreType());
                } else {
                    mv.visitVarInsn(ILOAD, 1);
                    mv.visitMethodInsn(INVOKESTATIC, fm.getClass().getName().replace('.', '/'), "newArrayOfField", "(I)" + fm.bcLStoreType());
                }
                mv.visitFieldInsn(PUTFIELD, name + "Allocation", "m_" + fm.fieldName(), fm.bcLStoreType());
            }

            mv.visitInsn(RETURN);
            Label l14 = new Label();
            mv.visitLabel(l14);
            mv.visitLocalVariable("this", "L" + name + "Allocation;", null, l0, l14, 0);
            mv.visitLocalVariable("allocationSize", "I", null, l0, l14, 1);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }

    public static byte[] dumpElement(TypeModel tm) {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;

        String name = tm.bcType();

        cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, name + "Element", "L" + collections + "impl/AbstractHugeElement<L" + name + "Allocation;>;L" + name + ";", collections + "impl/AbstractHugeElement", new String[]{name});

        cw.visitSource(tm.type().getSimpleName() + "Element.java", null);

        {
            fv = cw.visitField(0, "allocation", "L" + name + "Allocation;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(L" + collections + "impl/AbstractHugeArrayList;J)V", "(L" + collections + "impl/AbstractHugeArrayList<L" + name + ";L" + name + ";L" + name + "Element;>;J)V", null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(33, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(LLOAD, 2);
            mv.visitMethodInsn(INVOKESPECIAL, collections + "impl/AbstractHugeElement", "<init>", "(L" + collections + "impl/AbstractHugeArrayList;J)V");
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLineNumber(34, l2);
            mv.visitInsn(RETURN);
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLocalVariable("this", "L" + name + "Element;", null, l0, l3, 0);
            mv.visitLocalVariable("list", "L" + collections + "impl/AbstractHugeArrayList;", "L" + collections + "impl/AbstractHugeArrayList<L" + name + ";L" + name + "Allocation;L" + name + "Element;>;", l0, l3, 1);
            mv.visitLocalVariable("n", "J", null, l0, l3, 2);
            mv.visitMaxs(4, 4);
            mv.visitEnd();
        }
        for (FieldModel fm : tm.fields()) {
            ///////// SETTER //////////

            int maxLocals = 2 + fm.bcFieldSize();

            mv = cw.visitMethod(ACC_PUBLIC, "set" + fm.titleFieldName(), "(" + fm.bcLFieldType() + ")V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);

            int invoke = INVOKESTATIC;
            if (fm.virtualGetSet()) {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, name + "Element", "list", "L" + collections + "impl/AbstractHugeArrayList;");
                mv.visitTypeInsn(CHECKCAST, name + "ArrayList");
                mv.visitFieldInsn(GETFIELD, name + "ArrayList", fm.fieldName() + "FieldModel", fm.bcLModelType());
                invoke = INVOKEVIRTUAL;
                maxLocals++;
            }

            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, name + "Element", "allocation", "L" + name + "Allocation;");
            mv.visitFieldInsn(GETFIELD, name + "Allocation", "m_" + fm.fieldName(), fm.bcLStoreType());
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, name + "Element", "offset", "I");
            mv.visitVarInsn(loadFor(fm.bcType()), 1);

            mv.visitMethodInsn(invoke, fm.bcModelType(), "set", "(" + fm.bcLStoreType() + "I" + fm.bcLSetType() + ")V");
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "L" + name + "Element;", null, l0, l2, 0);
            mv.visitLocalVariable("element", fm.bcLFieldType(), null, l0, l2, 1);
            mv.visitMaxs(maxLocals, 1 + fm.bcFieldSize());
            mv.visitEnd();

            ///////// GETTER //////////

            mv = cw.visitMethod(ACC_PUBLIC, "get" + fm.titleFieldName(), "()" + fm.bcLFieldType(), null, null);
            mv.visitCode();
            Label l3 = new Label();
            mv.visitLabel(l3);
            mv.visitLineNumber(46, l3);

            BCType bcType = fm.bcType();
            if (fm.virtualGetSet()) {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, name + "Element", "list", "L" + collections + "impl/AbstractHugeArrayList;");
                mv.visitTypeInsn(CHECKCAST, name + "ArrayList");
                mv.visitFieldInsn(GETFIELD, name + "ArrayList", fm.fieldName() + "FieldModel", fm.bcLModelType());
                bcType = BCType.Reference;
            }

            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, name + "Element", "allocation", "L" + name + "Allocation;");
            mv.visitFieldInsn(GETFIELD, name + "Allocation", "m_" + fm.fieldName(), fm.bcLStoreType());
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, name + "Element", "offset", "I");
            mv.visitMethodInsn(invoke, fm.bcModelType(), "get", "(" + fm.bcLStoreType() + "I)" + fm.bcLSetType());
            if (!fm.bcLSetType().equals(fm.bcLFieldType()))
                mv.visitTypeInsn(CHECKCAST, fm.bcFieldType());
            mv.visitInsn(returnFor(bcType));

            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLocalVariable("this", "L" + name + "Element;", null, l3, l4, 0);
            mv.visitMaxs(4, 2);
            mv.visitEnd();
        }

        {
            mv = cw.visitMethod(ACC_PROTECTED, "updateAllocation0", "(I)V", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(158, l0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, name + "Element", "list", "L" + collections + "impl/AbstractHugeArrayList;");
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, name + "Element", "index", "J");
            mv.visitMethodInsn(INVOKEVIRTUAL, collections + "impl/AbstractHugeArrayList", "getAllocation", "(J)Ljava/lang/Object;");
            mv.visitTypeInsn(CHECKCAST, name + "Allocation");
            mv.visitFieldInsn(PUTFIELD, name + "Element", "allocation", "L" + name + "Allocation;");
            Label l1 = new Label();
            mv.visitLabel(l1);
            mv.visitLineNumber(159, l1);
            mv.visitInsn(RETURN);
            Label l2 = new Label();
            mv.visitLabel(l2);
            mv.visitLocalVariable("this", "L" + name + "Element;", null, l0, l2, 0);
            mv.visitLocalVariable("allocationSize", "I", null, l0, l2, 1);
            mv.visitMaxs(4, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        final byte[] bytes = cw.toByteArray();
//        ClassReader cr = new ClassReader(bytes);
//        cr.accept(new ASMifierClassVisitor(new PrintWriter(System.out)),0);
        return bytes;
    }

    private static final int[] returnForArray = {IRETURN, LRETURN, DRETURN, FRETURN, ARETURN};

    private static int returnFor(BCType bcType) {
        return returnForArray[bcType.ordinal()];
    }

    private static final int[] loadForArray = {ILOAD, LLOAD, DLOAD, FLOAD, ALOAD};

    private static int loadFor(BCType bcType) {
        return loadForArray[bcType.ordinal()];
    }

    private static final int[] storeForArray = {ISTORE, LSTORE, DSTORE, FSTORE, ASTORE};

    private static int storeFor(BCType bcType) {
        return storeForArray[bcType.ordinal()];
    }
}
