(ns fractal.css
  (:require [garden.def :refer [defstyles]]
            [garden.units :as unit :refer [px em percent]]
            [garden.color :as color]))

(defn add-alpha [c a]
  (cond
   (color/color? c) (assoc c :alpha a)
   (color/hex? c) (add-alpha (color/as-rgb c) a)
   :default (add-alpha (color/from-name c) a)))

(defn rgba
  ([c a] (add-alpha c a))
  ([r g b a] (color/rgba r g b a)))


(def no-navbar-collapse
  [[:.navbar-collapse.collapse
    {:display :block!important
     :padding-bottom "0 !important"}]
   [:.container-fluid>.navbar-collapse
    :.container-fluid>.navbar-header
    :.container>.navbar-collapse
    :.container>.navbar-header
    {:margin-left "0 !important"
     :margin-right "0 !important"}]
   [:.navbar-header :.navbar-nav :.navbar-nav>li
    {:float :left!important}]
   [:.navbar-nav
    {:margin "0 !important"}]
   [:.navbar-nav>li>a
    {:padding-bottom "15px !important"
     :padding-top "15px !important"}]
   [:.navbar-nav.navbar-right:last-child
    {:margin-right "-15px"}]
   [:.navbar-right
    {:float :right!important}]
   [:.navbar-brand
    {:margin-left "-15px !important"}]])

(def navbar-style
  [:.navbar-default
   [:.navbar-nav>li>a:hover
    {:background-color (rgba :black 0.01)}]
   [:.navbar-brand
    {:color "#32f"
     :font-weight :bold
     :background-color (rgba "#eec" 0.3)}]])

(def noselect
  [[:.noselect
     {:-webkit-touch-callout :none
      :-webkit-user-select :none
      :-khtml-user-select :none
      :-moz-user-select :none
      :-ms-user-select :none
      :user-select :none}]
   [:div.noselect
    {:cursor :default}]])

(def app
  [:#app
   {:position :absolute
    :top 0
    :left 0
    :height (percent 100)
    :width (percent 100)}
   [:.wrapper
    {:display :flex
     :flex-direction :column
     :flex-wrap :nowrap
     :height (percent 100)
     :width (percent 100)}
    [:.navbar]
    [:.content
     {:flex-grow 1
      :overflow :hidden}]]])

(def fractal-content
  [:.content.fractal
   {:position :relative
    :display :flex
    :flex-direction :row
    :flex-wrap :nowrap}
   [:.fractal-control-bar
    {:min-width (em 10)
     :width (em 15)
     :margin-left (px 20)}
    [:.input
     {:margin 0
      :margin-top (em 1)
      }]]
   [:.fractal-canvas-div
    {:flex-grow 1
     :border [[(px 1) :solid "#ddd"]]
     :margin-right (px 20)
     :margin-bottom (px 20)
     :overflow :hidden}]])

(defstyles screen
  [noselect
   no-navbar-collapse
   navbar-style
   app
   fractal-content
   ])
