(ns my-project.core
  (:gen-class
    :name vision.kodai.clj.Main
    :implements [org.objectweb.asm.Opcodes])
  (:import
   (java.io DataOutputStream FileOutputStream)
   (org.objectweb.asm ClassWriter)
   (vision.kodai.clj Main)))

(defn -main []
  (let [cw (ClassWriter. 0)]
    (.visit
     cw
     (Main/V1_5)
     (+ (Main/ACC_PUBLIC) (Main/ACC_SUPER))
     "HelloFromASM"
     nil
     "java/lang/Object"
     nil)
    (let [init-method-visitor (.visitMethod cw (Main/ACC_PUBLIC) "<init>" "()V" nil nil)]
      (.visitCode init-method-visitor)
      (.visitVarInsn init-method-visitor (Main/ALOAD) 0)
      (.visitMethodInsn init-method-visitor (Main/INVOKESPECIAL) "java/lang/Object" "<init>" "()V")
      (.visitInsn init-method-visitor (Main/RETURN))
      (.visitMaxs init-method-visitor 1 1)
      (.visitEnd init-method-visitor))
    (let [main-method-visitor
          (.visitMethod cw (+ (Main/ACC_PUBLIC) (Main/ACC_STATIC)) "main" "([Ljava/lang/String;)V" nil nil)]
      (.visitCode main-method-visitor)
      (.visitFieldInsn main-method-visitor (Main/GETSTATIC) "java/lang/System" "out" "Ljava/io/PrintStream;")
      (.visitLdcInsn main-method-visitor "Hello from ASM")
      (.visitMethodInsn main-method-visitor
                        (Main/INVOKEVIRTUAL)
                        "java/io/PrintStream"
                        "println"
                        "(Ljava/lang/String;)V")
      (.visitInsn main-method-visitor (Main/RETURN))
      (.visitMaxs main-method-visitor 2 1)
      (.visitEnd main-method-visitor))
    (.visitEnd cw)
    (let [out (DataOutputStream. (FileOutputStream. "HelloFromASM.class"))]
      (.write out (.toByteArray cw))
      (.flush out)
      (.close out))))
