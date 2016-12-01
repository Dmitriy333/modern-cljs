(ns modern-cljs.dsl.dslparser
  (:require
    [clojure.string :as s]
    [modern-cljs.dsl.trigger-events :as t]))

(defn invoke-trigger-event-function [trigger-event-function rule]
  (binding [*ns* (:ns (meta #'t/trigger-simple-event))]
    (load-string (str "("trigger-event-function " \"" rule "\")"))))

(defn parse-rule [rule]
  (let [trigger-event (get (s/split rule #"\s+") 0) ]
    (let [trigger-event-function (s/replace trigger-event #"\$" "")]
      (invoke-trigger-event-function trigger-event-function rule))))


