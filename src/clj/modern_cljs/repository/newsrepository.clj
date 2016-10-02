(ns modern-cljs.repository.newsrepository
  (:require [compojure.core :refer :all]
            [clojure.java.jdbc :as sql]
            [modern-cljs.repository.dbconfig :as dbconfig]))

(defn load-all-news []
  (sql/query dbconfig/mysql-db ["SELECT * FROM news"]
             {:row-fn #(str (:short_text %) " was released in " (:creation_date %))}))

;(let [news (load-all-news)]
;  (println news))

(defn load-news-by-id [id]
  (sql/query dbconfig/mysql-db
             ["select * from news where news_id = ?" id]
             {:row-fn :short_text}))

(defn get-news-by-id [id]
  (sql/query dbconfig/mysql-db
             ["select * from news where news_id = ?" id]))