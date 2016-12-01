(ns modern-cljs.dsl.dsltask
  (:require [clojure.string :refer [split]]
            [modern-cljs.util.json-date-converter :as jsd]
            [clojure.data.json :as json]))

(defn send-string-details-by-email-task [entity email string-details]
  (println (str entity " " string-details " " email)))

(defn send-details-in-json-by-email-task [entity email details]
  (println (str entity " " (json/write-str details :value-fn jsd/convert-date-to-string) " " email)))














;(defmulti execute
;          (fn [form]
;            (let [tokens (split form #" ")]
;                (get tokens 1))))
;
;(defmethod execute
;  "login"
;  [form]
;  (let [tokens (split form #" ")]
;    (let [event (get tokens 2) args (get tokens 4)]
;      (let [eventHandler (eventHolder (keyword event))]
;        (eventHandler args))
;
;      )
;
;
;    ))
;
;
;(defn execute-code [dsl-string]
;  (binding [*ns* (:ns (meta #'send-email))]
;    (let [tokens (split dsl-string #" ")]
;      (load-string (str  "(execute \"" dsl-string "\")"))))
;
;
;  )




































(defn execute-custom-dsl [customDsl]
  (let [tokens (split str #" ")]
    (let [action (get tokens 1) email (get tokens 4)]





      ;(println (str ""))






      ;(let [executable-code (str "(" action email ")")]
      ;  (println executable-code)
      ;  ;(load-string executable-code)
      ;  )
      (load-string "(println \"abc\")")




      )))