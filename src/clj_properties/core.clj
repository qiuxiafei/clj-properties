(ns clj-properties.core
  (:require [clojure.string :as str])
  (:require [clojure.java.io :as io]))

(defn get-lines [file]
  (with-open [rdr (io/reader file)]
    (doall (line-seq rdr))))

(defn read-conf [file]
  (into {}
    (->> (get-lines file)
      (map str/trim)
      (filter #(> (.length %) 0))
      (filter (complement #(.startsWith % "#")))
      (map #(str/split % #" *= *"))
      (map (fn [[k v]] [(keyword k) v])))))
