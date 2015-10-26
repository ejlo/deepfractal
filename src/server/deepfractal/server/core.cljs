(ns deepfractal.server.core
  (:require [cljs.nodejs :as node]))

(node/enable-util-print!)

(defonce express (node/require "express"))
(defonce serve-static (node/require "serve-static"))

(defn say-hello! [req res]
  (.send res "Hello world!"))

(defn main []
  (let [app (express)
        port 3001]
    (doto app
      (. (use (serve-static "resources/public" #js {:index "index.html"})))
      (. listen port (fn [] (println "Server started on port" port))))))

(set! *main-cli-fn* main)
