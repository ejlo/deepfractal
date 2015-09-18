(ns deepfractal.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/register-sub
 :active-panel
 (fn [db _]
   (reaction (:active-panel @db))))


(re-frame/register-sub
 :canvas-size
 (fn [db]
   (reaction (:canvas-size @db))))

(re-frame/register-sub
 :fractal-params
 (fn [db]
   (reaction (:fractal-params @db))))
