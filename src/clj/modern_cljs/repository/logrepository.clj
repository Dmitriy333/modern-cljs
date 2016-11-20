(ns modern-cljs.repository.logrepository
  (:require [compojure.core :refer :all]
            [clojure.java.jdbc :as jdbc]
            [modern-cljs.config.dbconfig :as dbconfig]
            [modern-cljs.model.model :as model]
            [modern-cljs.repository.crudrepository :as crudRepository]))

(defrecord LogRepositoryImpl [dbconfig]
  crudRepository/CrudRepository
  (find-by-id [this id]
    (let [item (first (jdbc/query dbconfig ["select * from log where id = ?" id]))]
      (model/->Log (item :id) (item :value) (item :type) (item :creation_date))))
  (find-all [this]
    (let [list (jdbc/query dbconfig ["SELECT * from log order by creation_date DESC"])]
      (for [item list]
        (model/->Log (item :id) (item :value) (item :type) (item :creation_date)))))
  (add [this model]
    (jdbc/insert! dbconfig :log model))
  (delete [this id] (jdbc/delete! dbconfig :log ["id = ?" id])))

(def logRepositoryComponent (->LogRepositoryImpl dbconfig/mysql-db))

