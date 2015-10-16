(ns deepfractal.handlers
  (:require [re-frame.core :as re-frame]
            [deepfractal.db :as db]
            [deepfractal.utils :as utils]
            [deepfractal.math.coords :as coords]))

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
 :set-center-x
 (fn [db [_ cx]]
   (assoc-in db [:fractal-params :center 0] cx)))

(re-frame/register-handler
 :set-center-y
 (fn [db [_ cy]]
   (assoc-in db [:fractal-params :center 1] cy)))


(re-frame/register-handler
 :set-zoom
 (fn [db [_ zoom]]
   (assoc-in db [:fractal-params :zoom] zoom)))

(re-frame/register-handler
 :set-max-n
 (fn [db [_ max-n]]
   (if (pos? max-n)
     (assoc-in db [:fractal-params :max-n] max-n)
     db)))

(re-frame/register-handler
 :canvas-zoom
 (fn [{:keys [fractal-params canvas-size] :as db} [_ canvas-coords zoom-factor]]
   (let [{:keys [center zoom]} fractal-params
         center (mapv utils/->float center)
         zoom (utils/->float zoom)
         fractal-coords (coords/canvas->fractal canvas-size center zoom canvas-coords)
         new-center (coords/zoom center fractal-coords zoom-factor)]
     (-> db
         (assoc-in [:fractal-params :zoom]
                   (utils/round-to-significant-figures (* zoom zoom-factor) 2.5))
         (assoc-in [:fractal-params :center] new-center)))))




(defonce current-id (atom 0))

(defn get-new-id! []
  (swap! current-id inc)
  @current-id)

(re-frame/register-handler
 :color-editor-add-node
 (fn [db [_ class p]]
   (println "(color-svg-add-node)" p)
   (update-in db [:color-editor :paths class] conj [(get-new-id!) p])))

(re-frame/register-handler
 :color-editor-move-point
 (fn [db [_ class k new-point]]
   (assoc-in db [:color-editor :paths class k] new-point)))
