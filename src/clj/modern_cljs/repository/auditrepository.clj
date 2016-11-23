(ns modern-cljs.repository.auditrepository
  (:require [compojure.core :refer :all]
            [clojure.java.jdbc :as jdbc]
            [modern-cljs.config.dbconfig :as dbconfig]
            [modern-cljs.model.model :as model]
            [modern-cljs.repository.crudrepository :as crudRepository]))

(defrecord AuditRepositoryImpl [dbconfig]
  crudRepository/CrudRepository
  (find-by-id [this id]
    (let [item (first (jdbc/query dbconfig ["select * from audit where id = ?" id]))]
      (model/->Log (item :id) (item :value) (item :type) (item :creation_date))))
  (find-all [this]
    (let [list (jdbc/query dbconfig ["SELECT * from audit"])]
      (for [item list]
        (model/->Audit (item :id) (item :news_id) (item :user_id) (item :creation_date)))))
  (add [this model]
    (jdbc/insert! dbconfig :audit model))
  (delete [this id] (jdbc/delete! dbconfig :log ["id = ?" id])))

(def auditRepositoryComponent (->AuditRepositoryImpl dbconfig/mysql-db))

