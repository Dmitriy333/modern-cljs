(ns modern-cljs.repository.dslrepository
  (:require [compojure.core :refer :all]
            [clojure.java.jdbc :as jdbc]
            [modern-cljs.config.dbconfig :as c]
            [modern-cljs.model.model :as model]))

(defn find-rule [entity entity-event]
      (let [item (first (jdbc/query c/mysql-db ["select * from rule where entity = ? and entity_event = ?" entity entity-event]))]
        (model/->Rule (item :id) (item :entity) (item :entity_event) (item :task))))

(defn save-rule [rule]
  (jdbc/insert! c/mysql-db :rule rule))
