(ns modern-cljs.dsl.trigger-events
  (:require [clojure.string :refer [split]]
            [modern-cljs.model.model :as model]
            [modern-cljs.repository.dslrepository :as dr])

  )


  ;example: "$trigger_simple_event on entity entity-event dsltask"
  ;;this method should be invoked by dsl-parser
(defn trigger-simple-event [dsl-string]
  (let [tokens (split dsl-string #"\s+")]
    (let [rule (model/->Rule nil (get tokens 2) (get tokens 3) (get tokens 4))]
      (dr/save-rule rule))))


;; here could be smth like a trigger-difficult-event -> it will be parsed in its own way
;; and than stored into rule db table
