class AddImageName < ActiveRecord::Migration
 def change
  	 change_table :images do |t|
		t.string :image_name
  	end
 end
end
