(ns modern-cljs.dsl.actiondsl
  (:import (clojure.lang PersistentList)))

(defmulti emit-bash
          (fn [form]
            (class form)))
(defmethod emit-bash
  clojure.lang.PersistentList
  [form]
  (case (name (first form))
    "println" (str "echo " (second form))
    nil))
(defmethod emit-bash
  java.lang.String
  [form]
  form)
(defmethod emit-bash
  java.lang.Integer
  [form]
  (str form))
(defmethod emit-bash
  java.lang.Double
  [form]
  (str form))

(emit-bash "a")

(defmacro script [form]
  `(emit-bash '~form))


(defmacro with-implementation
  [body]
  `(~@body))

;(defmacro with-implementation
;  [body]
;  `~@body)

(with-implementation
   (script
     (println "a")))


(emit-bash
    '(println "a"))