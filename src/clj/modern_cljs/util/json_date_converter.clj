(ns modern-cljs.util.json-date-converter
  (:import (java.sql Timestamp)))

(defn convert-date-to-string [key value]
  (if (= key :creation_date)
    (str (Timestamp. (.getTime value)))
    value))
