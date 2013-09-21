class AddLattitudeAndLongitude < ActiveRecord::Migration
  def change
  	change_table :images do |t|
  	  t.string :lat
  	  t.string :lng
  	end
  end
end
