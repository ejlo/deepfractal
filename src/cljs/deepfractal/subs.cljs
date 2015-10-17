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

(re-frame/register-sub
 :color-editor-svg-bounding-rect
 (fn [db]
   (reaction (get-in @db [:color-editor :svg-bounding-rect]))))

(re-frame/register-sub
 :color-editor-paths
 (fn [db]
   (reaction (get-in @db [:color-editor :paths]))))


(re-frame/register-sub
 :color-editor-bcr
 (fn [db]
   (reaction (get-in @db [:color-editor :bcr]))))

(re-frame/register-sub
 :color-editor-point-radius
 (fn [db]
   (reaction (+ 3 (/ (get-in @db [:color-editor :bcr :height]) 100)))))


(re-frame/register-sub
 :color-editor-y-scale
 (fn [db]
   (reaction (/ (get-in @db [:color-editor :bcr :height]) 100))))
