(define (problem C3_2)
  (:domain shakeyworld)
  (:objects
   room1 room2 room3				;; there are three rooms,
   light1 light2 light3		      		;; one light in each room
   box1	box2	            			;; two boxes
   shakey1      	      			;; shakey
   pencil1 pencil2 pencil3			;; three small pencils
   toy1 toy2	   				;; two small toys
   )
  (:init
   ;; Type declarations:

   ;; box1 and shakey1 are both "objects"
   (object box1) (object box2) (object shakey1)
   (object pencil1)(object pencil2)(object pencil3)
   (object toy1) (object toy2)
   
   ;; shakey1 is of type "shakey"
   (shakey shakey1)

   ;; box1 is of type "box"
   (box box1) (box box2)

   ;; pencils and toys are of type "small object"
   (small_object pencil1)(small_object pencil2)(small_object pencil3)
   (small_object toy1)   (small_object toy2)
   

   ;; all rooms is of type "room", 
   (room room1) (room room2) (room room3)

   ;; and lightswitches as "light".
   (light light1) (light light2) (light light3) 

   ;; "loc" defines the static location for lights
   (loc light1 room1) (loc light2 room2) (loc light3 room3)
   
   ;; doors defines the relation between rooms
   (door_wide room1 room2)
   (door_wide room2 room3)
   (door_narrow room2 room3)

   (door_wide room2 room1)
   (door_wide room3 room2)
   (door_narrow room3 room2)
      

   ;; The actual initial state of the problem, which specifies the
   ;; initial locations of all packages and all vehicles:
   (at box1 room1)
   (at box2 room3)
   (at shakey1 room1)
   (at pencil1 room1)
   (at pencil2 room2)
   (at pencil3 room1)
   (at toy1 room3)
   (at toy2 room3)
   )

   ;; The goal of the problem:
   (:goal (and (at shakey1 room1) (at box1 room3)(not(switch light1)) (not(switch light2)) (not(switch light3))
     	     	 (at pencil1 room3) (at pencil2 room1) (at pencil3 room3)
		 (at toy1 room1) (at toy2 room1)))
  )