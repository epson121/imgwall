class AddTagToDb < ActiveRecord::Migration
  def change
  	 change_table :images do |t|
		t.string :tag
  	end
  end
end
