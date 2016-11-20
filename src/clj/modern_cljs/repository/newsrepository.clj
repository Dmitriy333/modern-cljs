(ns modern-cljs.repository.newsrepository
  (:require [compojure.core :refer :all]
            [clojure.java.jdbc :as jdbc]
            [modern-cljs.config.dbconfig :as dbconfig]
            [modern-cljs.model.model :as model]))


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

;instance of news repo
(def news-repo-component (->NewsRepositoryComponent dbconfig/mysql-db))