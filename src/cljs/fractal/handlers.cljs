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
 :set-canvas-size
 (fn [db [_ dim]]
   (assoc db :canvas-size dim)))




(re-frame/register-handler
 :set-center
 (fn [db [_ cx cy]]
   (assoc-in db [:fractal-params :center] [cx cy])))

(re-frame/register-handler
 :set-zoom
 (fn [db [_ zoom]]
   (assoc-in db [:fractal-params :zoom] zoom)))

(re-frame/register-handler
 :set-max-n
 (fn [db [_ max-n]]
   (assoc-in db [:fractal-params :max-n] max-n)))
