(ns modern-cljs.repository.newsrepository
  (:require [compojure.core :refer :all]
            [clojure.java.jdbc :as sql]
            [modern-cljs.config.dbconfig :as dbconfig]
            [modern-cljs.model.model :as model]))

;(defrecord News [id, title, short-text, full-text, creation-date])

(defprotocol CrudRepository
  (find-all [this])
  (find-by-id [this id]))

(defrecord NewsRepositoryComponent [dbconfig]
  CrudRepository
  (find-by-id [this id]
    (let [news-items (sql/query dbconfig ["select * from news where news_id = ?" id])]
      (for [news news-items]
        (model/->News (news :news_id) (news :title) (news :short_text) (news :full_text) (news :creation_date)))))
  (find-all [this]
    (let [news-list (sql/query dbconfig ["SELECT * FROM news"])]
      (for [news news-list]
        (model/->News (news :news_id) (news :title) (news :short_text) (news :full_text) (news :creation_date))))))

;instance of news repo
(def news-repo-component (->NewsRepositoryComponent dbconfig/mysql-db))

(defn news-repo-find-all-example []
  (let [news-list (find-all news-repo-component)]
    (for [news news-list]
      (println news))))

(defn news-repo-find-by-id-example [id]
  (let [news (find-by-id news-repo-component id)]
    (println (:id news) )))

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