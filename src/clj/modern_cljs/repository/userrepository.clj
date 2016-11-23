(ns modern-cljs.repository.userrepository
  (:require [compojure.core :refer :all]
            [clojure.java.jdbc :as jdbc]
            [modern-cljs.config.dbconfig :as dbconfig]
            [modern-cljs.model.model :as model]
            [modern-cljs.repository.crudrepository :as crudRepository]))

(defprotocol UserRepository
  (find-by-login [this login])
  (find-by-email-and-hash [this email hash]))

(defrecord UserRepositoryImpl [dbconfig]
  crudRepository/CrudRepository UserRepository

  (find-by-id [this id]
    (let [item (first (jdbc/query dbconfig ["select * from users where id = ?" id]))]
      (model/->User (item :id) (item :email) (item :login) (item :password))))
  (find-all [this]
    (let [items (jdbc/query dbconfig ["SELECT * from users"])]
      (for [item items]
        (model/->User (item :id) (item :email) (item :login) (item :password)))))
  (add [this model]
    (jdbc/insert! dbconfig :users model))
  (delete [this id] (jdbc/delete! dbconfig :users ["id = ?" id]))

  (find-by-login [this login]
    (let [item (first (jdbc/query dbconfig ["select * from users where login = ?" login]))]
      (model/->User (item :id) (item :email) (item :login) (item :password))))

  (find-by-email-and-hash [this email hash]
    (jdbc/query dbconfig ["select * from users where email = ? and password = ?" email hash])))

(def userRepositoryComponent (->UserRepositoryImpl dbconfig/mysql-db))
