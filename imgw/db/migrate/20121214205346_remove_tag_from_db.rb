class RemoveTagFromDb < ActiveRecord::Migration
  change_table :images do |t|
	t.remove :tag
  end
end
