(ns modern-cljs.config.dbconfig
  (:require [compojure.core :refer :all]))

(def mysql-db {
   :classname "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :subname "//127.0.0.1:3306/newsmanagement"
   :user "root"
   :password "root"})


