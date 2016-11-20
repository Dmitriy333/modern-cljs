(ns modern-cljs.repository.commentsrepository
  (:require [modern-cljs.model.model :as model]
            [clojure.java.jdbc :as jdbc]
            [modern-cljs.config.dbconfig :as dbconfig]
            [modern-cljs.repository.newsrepository :as repository])
)

(defprotocol CommentsRepository
  (find-by-news-id [this newsId]))

(defrecord CommentsRepositoryImpl [dbconfig]
  repository/CrudRepository CommentsRepository

  (find-by-id [this id]
    (let [item (first (jdbc/query dbconfig ["SELECT * FROM COMMENTS WHERE id = ?" id]))]
      (model/->Comment (item :id) (item :text) (item :creation_date) (item :news_id) (item :user_id))))

  (find-all [this]
    (let [itemList (jdbc/query dbconfig ["SELECT * FROM comments"])]
      (for [item itemList]
        (model/->Comment (item :id) (item :text) (item :creation_date) (item :news_id) (item :user_id)))))

  (add [this model]
    (let [resultSet (jdbc/insert! dbconfig :comments model)] resultSet))

  (delete [this id] (jdbc/delete! dbconfig :comments ["id = ?" id]))

  (find-by-news-id [this newsId]
    (let [itemList (jdbc/query dbconfig ["SELECT * FROM comments where news_id = ?" newsId])]
      (for [item itemList]
        (model/->Comment (item :id) (item :text) (item :creation_date) (item :news_id) (item :user_id))))))

(def CommentRepositoryComponent (->CommentsRepositoryImpl dbconfig/mysql-db))
