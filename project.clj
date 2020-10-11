(defproject my-project "0.1.0-SNAPSHOT"
  :description "My project"
  :url "http://github.com/0918nobita"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"] [org.ow2.asm/asm "9.0"]]
  :repl-options {:init-ns my-project.core}
  :main my-project.core
  :aot [my-project.core])
