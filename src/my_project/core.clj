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
      (doto
       init-method-visitor
        (.visitCode)
        (.visitVarInsn (Main/ALOAD) 0)
        (.visitMethodInsn (Main/INVOKESPECIAL) "java/lang/Object" "<init>" "()V")
        (.visitInsn (Main/RETURN))
        (.visitMaxs 1 1)
        (.visitEnd)))
    (let [main-method-visitor
          (.visitMethod cw (+ (Main/ACC_PUBLIC) (Main/ACC_STATIC)) "main" "([Ljava/lang/String;)V" nil nil)]
      (doto main-method-visitor
        (.visitCode)
        (.visitFieldInsn (Main/GETSTATIC) "java/lang/System" "out" "Ljava/io/PrintStream;")
        (.visitLdcInsn "Hello from ASM")
        (.visitMethodInsn (Main/INVOKEVIRTUAL) "java/io/PrintStream" "println" "(Ljava/lang/String;)V")
        (.visitInsn (Main/RETURN))
        (.visitMaxs 2 1)
        (.visitEnd)))
    (.visitEnd cw)
    (doto (DataOutputStream. (FileOutputStream. "HelloFromASM.class"))
      (.write (.toByteArray cw))
      (.flush)
      (.close))))
