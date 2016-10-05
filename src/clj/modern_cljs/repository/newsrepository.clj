(ns modern-cljs.repository.newsrepository
  (:require [compojure.core :refer :all]
            [clojure.java.jdbc :as sql]
            [modern-cljs.config.dbconfig :as dbconfig]))

(defrecord News [id, title, short-text, full-text, creation-date])

(defprotocol CrudRepository
  (find-all [x])
  (find-by-id [id]))

(defrecord NewsRepositoryComponent [dbconfig]
  CrudRepository
  (find-all [x]
    (let [news-list (sql/query dbconfig ["SELECT * FROM news"])]
      (for [news news-list]
        (->News (news :news_id) (news :title) (news :short_text) (news :full_text) (news :creation_date)))))
  (find-by-id [id]
    ("abc"))
  )

;instance of news repo
(def news-repo-component (NewsRepositoryComponent. dbconfig/mysql-db))

(defn news-repo-find-all-example []
  (let [news-list (find-all news-repo-component)]
    (for [news news-list]
      (println news))))

(defn load-all-news []
  (sql/query dbconfig/mysql-db ["SELECT * FROM news"]
             {:row-fn #(str (:short_text %) " was released in " (:creation_date %))}))

(defn load-news-by-id [id]
  (sql/query dbconfig/mysql-db
             ["select * from news where news_id = ?" id]
             {:row-fn :short_text}))

(defn get-news-by-id [id]
  (sql/query dbconfig/mysql-db
             ["select * from news where news_id = ?" id]))

(defn save-news [news]
  news)