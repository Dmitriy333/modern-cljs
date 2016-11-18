(ns modern-cljs.repository.newsrepository
  (:require [compojure.core :refer :all]
            [clojure.java.jdbc :as jdbc]
            [modern-cljs.config.dbconfig :as dbconfig]
            [modern-cljs.model.model :as model]))

;(defrecord News [id, title, short-text, full-text, creation-date])

(defprotocol CrudRepository
  (find-all [this])
  (find-by-id [this id])
  (add [this model])
  (delete [this id]))

(defrecord NewsRepositoryComponent [dbconfig]
  CrudRepository
  (find-by-id [this id]
    (let [news-item (first (jdbc/query dbconfig ["select * from news where news_id = ?" id]))]
        (model/->News (news-item :news_id) (news-item :title) (news-item :short_text) (news-item :full_text) (news-item :creation_date))))
  (find-all [this]
    (let [news-list (jdbc/query dbconfig ["SELECT * FROM news"])]
      (for [news news-list]
        (model/->News (news :news_id) (news :title) (news :short_text) (news :full_text) (news :creation_date)))))
  (add [this model]
    (jdbc/insert! dbconfig :news model))
  (delete [this id] (jdbc/delete! dbconfig :news ["id = ?" id])))

;(defrecord NewsRepositoryComponent [dbconfig]
;  CrudRepository
;  (find-by-id [this id]
;    (let [news-items (jdbc/query dbconfig ["select * from news where news_id = ?" id])]
;      (println (class news-items))
;      (for [news news-items]
;        (model/->News (news :news_id) (news :title) (news :short_text) (news :full_text) (news :creation_date)))))
;  (find-all [this]
;    (let [news-list (jdbc/query dbconfig ["SELECT * FROM news"])]
;      (for [news news-list]
;        (model/->News (news :news_id) (news :title) (news :short_text) (news :full_text) (news :creation_date)))))
;  (add [this model]
;    (jdbc/insert! dbconfig :news model))
;  (delete [this id] (jdbc/delete! dbconfig :news ["id = ?" id])))

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
  (jdbc/query dbconfig/mysql-db ["SELECT * FROM news"]
              {:row-fn #(str (:short_text %) " was released in " (:creation_date %))}))

(defn load-news-by-id [id]
  (jdbc/query dbconfig/mysql-db
              ["select * from news where news_id = ?" id]
              {:row-fn :short_text}))

(defn get-news-by-id [id]
  (jdbc/query dbconfig/mysql-db
              ["select * from news where news_id = ?" id]))

(defn save-news [news]
  news)