(ns core
  (:require
    [clojure.core.async :as a :refer [<!! >!!]]
    [clojure.java.io :as io]
    [duratom.core :as core]
    [etaoin.api :as e])
  (:import
    (java.nio.file
      Files
      LinkOption
      Paths)))


(defonce CLOJURE_UNANSWERED_URL "https://stackoverflow.com/questions/tagged/clojure?tab=Unanswered
")


(defonce CLOJURE_NEWEST_URL "https://stackoverflow.com/questions/tagged/clojure?tab=Newest")

(defonce DATA_DIR_PATH (or (System/getenv "SO_DATA_DIR") "data"))

(defonce driver (e/firefox))


(defn get-unanswared-questions
  []
  (e/go driver CLOJURE_UNANSWERED_URL)
  (->> (e/query-tree driver :questions {:tag :div})
       (map #(e/get-element-text-el driver %))))


(defn get-newest-questions
  []
  (e/go driver CLOJURE_NEWEST_URL)
  (->> (e/query-tree driver :questions {:tag :div})
       (map #(e/get-element-text-el driver %))))


(when (not (Files/exists (Paths/get (new java.net.URI (str "file:///" DATA_DIR_PATH)))
                         (into-array LinkOption [LinkOption/NOFOLLOW_LINKS])))
  (.mkdir (io/file DATA_DIR_PATH)))


(defonce unanswered-questions-db
  (core/duratom :local-file
                :file-path (.getPath
                             (io/file DATA_DIR_PATH "unanswered-questions"))
                :init []))


(defonce newest-questions-db
  (core/duratom :local-file
                :file-path (.getPath
                             (io/file DATA_DIR_PATH "newest-questions"))
                :init []))


(defn -main
  [opts]
  (let [c (a/chan)
        _ (>!! c (get-unanswared-questions))
        _ (swap! unanswered-questions-db conj (<!! c))
        _ (>!! c (get-newest-questions))
        _ (swap! newest-questions-db conj (<!! c))]
    (a/close! c)))
