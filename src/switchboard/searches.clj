;; Search algorithms too small to really need their own module/file.

(ns switchboard.searches
  (:require [switchboard.utils :refer :all]
            [org.httpkit.client :as http]))


;; Urban Dictionary
(def ud (partial build-url "http://urbandictionary.com"))
(defn urbandictionary [rest]
  (if (nil? rest)
    (ud)
    (ud (str "define.php?term=" rest))))


;; Magic: the Gathering cards (via magiccards.info)
;; TODO: good candidate for simple search refactor/module? but is only one to
;; have different site for base vs rest cases...
(defn mtg [rest]
  (if (nil? rest)
    "http://mtgsalvation.com"
    ;; NOTE: v=scan means default to just-the-cards view, which is a nice list
    ;; for multiple-hit results; the site automagically displays full view if
    ;; only one hit, too. Less work for me!
    (str "http://magiccards.info/query?v=scan&q=" rest)))


;; My Pinboard bookmarks & tags
(def pb (partial build-url "https://pinboard.in"))
(def pb-user "u:bitprophet")
(defn pinboard [rest]
  (if (nil? rest)
    (pb pb-user)
    (let [rest (http/url-encode rest)
          tag-url (pb pb-user (str "t:" rest))]
      ; Sadly, an 'empty' page of bookmarks isn' a 404 or similar, so...we do
      ; this instead. Easier than using auth + API for now.
      (if-not (.contains (@(http/get tag-url) :body) "<span class=\"bookmark_count\">0</span>")
        tag-url
        (pb "search" pb-user (str "?query=" rest))))))


;; Google Image Search...tiny bit quicker than mashing in its domain? ehhh
(defn gis [rest]
  (if (nil? rest)
    ;; Empty search, while unlikely, can just take us to images.google.com -
    ;; good for e.g. reverse image lookup. ¯\_(ツ)_/¯
    "https://images.google.com/"
    ;; Regular ol' GIS otherwise
    (str "https://www.google.com/search?tbm=isch&q=" rest)))


;; Anime News Network
(def -ann (partial build-url "http://www.animenewsnetwork.com"))
(defn ann [rest]
  (if (nil? rest)
    (-ann)
    (-ann "encyclopedia" "search" (str "name?q=" rest))))
