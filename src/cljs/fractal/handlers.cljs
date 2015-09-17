(ns fractal.handlers
    (:require [re-frame.core :as re-frame]
              [fractal.db :as db]))

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/register-handler
 :set-active-panel
 (fn [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/register-handler
 :change-canvas-size
 (fn [db [_ dim]]
   (assoc db :canvas-size dim)))
