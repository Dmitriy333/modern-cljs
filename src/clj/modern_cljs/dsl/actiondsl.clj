(ns modern-cljs.dsl.actiondsl
  (:require [clojure.string :refer [split]]
            [modern-cljs.repository.logrepository :as lr]))



(defn send-email [email]
  (println (str "email was sended to " email)))

(def eventHolder {:send-email send-email})


(defmulti execute
          (fn [form]
            (let [tokens (split form #" ")]
                (get tokens 1))))

(defmethod execute
  "login"
  [form]
  (let [tokens (split form #" ")]
    (let [event (get tokens 2) args (get tokens 4)]
      (let [eventHandler (eventHolder (keyword event))]
        (eventHandler args)))))


(defmacro execute-custom-code [fun & args]
  ;(cons fun (reverse args))
  ()

  )





(defmacro script [form]
  `(println '~form))


(defmacro with-implementation
  [body]
  `(~@body))


;(with-implementation
;   (script
;     (println "a")))

;
;(emit-bash
;    '(println "a"))