(ns modern-cljs.service.logservice
  (:use
    :require
    [modern-cljs.repository.newsrepository]
    [modern-cljs.repository.commentsrepository]
    [modern-cljs.repository.logrepository]
    [ring.util.response :as response])
  (:require [modern-cljs.model.model :as model]
            [modern-cljs.repository.crudrepository :as crudRepository]
            [modern-cljs.repository.logrepository :as logRepository]
            [modern-cljs.service.logservice :as logService]
            [clojure.data.json :as json])
  (:import (java.util Date)
           (java.sql Timestamp)))


(def log-agent (agent logRepositoryComponent))

;(defn my-value-writer [key value]
;  (if (= key :creation_date)
;    (str (Timestamp. (.getTime value)))
;    value))
;
;(defn write-out [message logger]
;  (let [item-message message]
;    (println item-message)
;    (println logger)
;    ;(crudRepository/add logger item-message)
;    )
;  )
;
;(defn log-message [logger message]
;  ;(println message)
;  (send logger write-out message))
;
;(defn log-event [event type]
;  (let [event-to-log (model/->Log nil (json/write-str event :value-fn my-value-writer) type (new Date))]
;    (log-message log-agent event-to-log)))

