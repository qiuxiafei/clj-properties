(ns clj-properties.core
  (:require [clojure.string :as str])
  (:require [clojure.java.io :as io]))

(defn- get-lines [file]
  (with-open [rdr (io/reader file)]
    (doall (line-seq rdr))))

(defn- clojurefy [kv]
  (into {}
    (map (fn [[k v]] [(keyword k) v]) kv)))

(defn- validate-line! [line]
  (if-not (re-matches #".+ *= *.+" line)
    (throw (RuntimeException. (str "Invalid line: " line)))
    line))

(defn- parse-file [file]
  (->> (get-lines file)
    (map str/trim)
    (filter #(> (.length %) 0))
    (filter (complement #(.startsWith % "#")))
    (map validate-line!)
    (map #(str/split % #" *= *"))
    ))

(defn read-conf [file]
  (->> file parse-file clojurefy))

(defn read-resource [uri]
  (->> uri io/resource parse-file clojurefy))
