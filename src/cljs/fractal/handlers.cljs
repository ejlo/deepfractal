(ns fractal.handlers
    (:require [re-frame.core :as re-frame]
              [fractal.db :as db]
              [fractal.math.coords :as coords]))

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

(re-frame/register-handler
 :canvas-zoom
 (fn [{:keys [fractal-params canvas-size] :as db} [_ canvas-coords zoom-factor]]
   (let [{:keys [center zoom]} fractal-params
         fractal-coords (coords/canvas->fractal canvas-size center zoom canvas-coords)
         new-center (coords/zoom center fractal-coords zoom-factor)]
     (-> db
         (assoc-in [:fractal-params :zoom] (* zoom zoom-factor))
         (assoc-in [:fractal-params :center] new-center)))))
