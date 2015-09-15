(ns fractal.handlers
    (:require [re-frame.core :as re-frame]
              [fractal.db :as db]))

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))
